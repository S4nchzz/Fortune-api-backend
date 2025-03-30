package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.enums.IdentityDocument;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.log.Log;
import com.fortune_api.security.dto.AuthResponse;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UProfileService userProfileService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "password") final String password) {
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
        String token = jwtUtils.generateToken(user.getId());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public UserEntity register(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        final String salt = BCrypt.gensalt();
        final String passwordHashed = BCrypt.hashpw(password, salt);

        return userService.register(identityDocument, email, salt, passwordHashed);
    }

    @PostMapping("/createDigitalSign")
    public UserEntity createDigitalSign(@RequestParam(name = "digital_sign") final int pin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserById(user.getId());

        if (userEntity.getDigital_sign() != null) {
            return null; // For some reason the user has already a digital sign
        }

        userEntity.setDigital_sign(pin);
        userService.save(userEntity);

        return userEntity;
    }

    public static IdentityDocument documentType(final String identityDocument) {
        if (identityDocument == null || identityDocument.isEmpty()) {
            return null;
        }

        int letterCount = 0;
        int numberCount = 0;
        for (char c : identityDocument.toCharArray()) {
            if (c >= 65 && c <= 90) {
                letterCount++;
            }

            if (c >= 48 && c <= 57) {
                numberCount++;
            }
        }

        return Character.isAlphabetic(identityDocument.charAt(identityDocument.length() - 1)) && letterCount == 1 && numberCount == 8 && identityDocument.length() == 9 ? IdentityDocument.DNI : IdentityDocument.NIE;
    }
}