package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import com.fortune_api.security.jwt.JwtUtils;
import org.apache.coyote.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UProfileService userProfileService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @PostMapping("/createDigitalSign")
    public ResponseEntity<?> createDigitalSign(@RequestBody() String createDigitalSignRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserById(user.getId());

        if (userEntity.getDigital_sign() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        JSONObject createDigitalSignRequestJSON = new JSONObject(createDigitalSignRequest);
        final int pin = createDigitalSignRequestJSON.getInt("digital_sign");

        userEntity.setDigital_sign(pin);
        userService.save(userEntity);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getProfile")
    public UserProfileEntity getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserById(user.getId());
        return userProfileService.findProfileByUserId(userEntity.getId());
    }

    @GetMapping("/getUpdateProfile")
    public ResponseEntity<?> getUpdateProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(409).build();
        }

        UserProfileEntity uProfile = userProfileService.findById(user.getId());

        if (uProfile == null) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.ok(new JSONObject()
                .put("name", uProfile.getName())
                .put("address", uProfile.getAddress())
                .put("identity_document", user.getIdentity_document())
                .put("email", user.getEmail())
                .put("phone", uProfile.getPhone())
                .put("pfp", uProfile.getPfp())
                .toString()
        );
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody String updateJSONData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(409).build();
        }

        UserProfileEntity uProfile = userProfileService.findById(user.getId());
        if (uProfile == null) {
            return ResponseEntity.status(409).build();
        }

        JSONObject decoded = new JSONObject(updateJSONData);
        final String name = decoded.getString("name");
        final String address = decoded.getString("address");
        final String identityDocument = decoded.getString("identityDocument");
        final String email = decoded.getString("email");
        final String phone = decoded.getString("phone");

        user.setEmail(email);
        user.setIdentity_document(identityDocument);

        uProfile.setPhone(phone);
        uProfile.setAddress(address);
        uProfile.setName(name);

        uProfile.setUser(user);

        UserProfileEntity profileUpdated = userProfileService.save(uProfile);
        UserEntity userUpdated = userService.save(user);

        if (profileUpdated == null || userUpdated == null) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.status(200).build();
    }

    @PostMapping("/getUserPhone")
    public ResponseEntity<?> getUserPhone(@RequestBody() String plain_body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(409).build();
        }

        JSONObject body = new JSONObject(plain_body);

        try {
            final long userID = body.getLong("userID");
            if (userService.findUserById(userID) == null) {
                return ResponseEntity.status(409).build();
            }

            final UserProfileEntity profile = userProfileService.findProfileByUserId(userID);
            if (profile == null) {
                return ResponseEntity.status(409).build();
            }

            return ResponseEntity.ok(new JSONObject()
                    .put("phone", profile.getPhone())
                    .toString());

        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }
}