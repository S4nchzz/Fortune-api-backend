package com.fortune_api.controller.auth;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.log.Log;
import com.fortune_api.network.response.AuthResponse;
import com.fortune_api.security.jwt.JwtUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UProfileService uProfileService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody() String loginRequest) {
        JSONObject loginRequestJson = new JSONObject(loginRequest);
        final String identityDocument = loginRequestJson.getString("identityDocument");
        final String password = loginRequestJson.getString("password");

        final String hashedPassword = userService.findHashedPasswordByIdentityDocument(identityDocument);
        if (hashedPassword == null) {
            Log.getInstance().writeLog("AuthController | hashedPassword is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final boolean isPasswordCorrect = BCrypt.checkpw(password, hashedPassword);

        if (!isPasswordCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserEntity user = userService.findUserByIdentityDocument(identityDocument);
        String token = jwtUtils.generateAccessToken(user.getId());

        AuthResponse authResponse = null;
        if (user.getDigital_sign() == null) {
            authResponse = new AuthResponse(token, false);
        } else {
            authResponse = new AuthResponse(token, true);
        }
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody() String registerRequest) {
        JSONObject registerRequestJSON = new JSONObject(registerRequest);
        String identityDocument = registerRequestJSON.getString("identityDocument");
        String password = registerRequestJSON.getString("password");
        String email = registerRequestJSON.getString("email");
        String name = registerRequestJSON.getString("name");
        String address = registerRequestJSON.getString("address");
        String phone = registerRequestJSON.getString("phone");
        String pfp = registerRequestJSON.getString("base64Image");

        final String salt = BCrypt.gensalt();
        final String passwordHashed = BCrypt.hashpw(password, salt);

        if (userService.findUserByIdentityDocument(identityDocument) != null || userService.findUserByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserEntity userEntity = userService.register(identityDocument, email, salt, passwordHashed);
        UserProfileEntity profile = uProfileService.createUserProfile(userEntity.getId(), name, address, phone, pfp, false);

        if (profile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @PostMapping("/signOperation")
    public ResponseEntity<?> signOperation(@RequestBody() String signOperationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        JSONObject signOperationRequestJSON = new JSONObject(signOperationRequest);
        final int digital_sign = signOperationRequestJSON.getInt("digital_sign");

        return ResponseEntity.ok(
                new JSONObject()
                        .put("operationAccepted", user.getDigital_sign() == digital_sign)
                        .toString()
        );
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody() String changePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(409).build();
        }

        JSONObject json = new JSONObject(changePassword);
        final String plainPassword = json.getString("password");

        final String passwordHashed = BCrypt.hashpw(plainPassword, user.getSalt());
        user.setPassword(passwordHashed);
        userService.save(user);

        return ResponseEntity.status(200).build();
    }

    @PostMapping("/changeDigitalSign")
    public ResponseEntity<?> changeDigitalSign(@RequestBody() String changeDigitalSign) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(409).build();
        }

        JSONObject json = new JSONObject(changeDigitalSign);
        final int plainDigitalSign = json.getInt("newDigitalSign");

        user.setDigital_sign(plainDigitalSign);
        this.userService.save(user);

        return ResponseEntity.status(200).build();
    }
}