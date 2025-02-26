package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.enums.IdentityDocument;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.log.Log;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UProfileService userProfileService;

    @GetMapping("/login")
    public UserEntity login(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "password") final String password) {
        final String hashedPassword = userService.findHashedPasswordByIdentityDocument(identityDocument);
        if (hashedPassword == null) {
            Log.getInstance().writeLog("AuthController | hashedPassword is null");
            return null;
        }

        final boolean isPasswordCorrect = BCrypt.checkpw(password, hashedPassword);

        if (!isPasswordCorrect) {
            return null;
        }

        return userService.findUserByIdentityDocument(identityDocument);
    }

    @PostMapping("/register")
    public UserEntity register(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        final String salt = BCrypt.gensalt();
        final String passwordHashed = BCrypt.hashpw(password, salt);

        return userService.register(identityDocument, email, salt, passwordHashed);
    }

    @PostMapping("/createDigitalSign")
    public UserEntity createDigitalSign(@RequestParam(name = "user_id") final long user_id, @RequestParam(name = "digital_sign") final int pin) {
        UserEntity user = userService.findUserById(user_id);

        if (user.getDigital_sign() != null) {
            return null; // For some reason the user has already a digital sign
        }

        user.setDigital_sign(pin);
        userService.save(user);

        return user;
    }

    @PostMapping("/updateProfileStatus")
    public UserEntity updateProfileStatus(@RequestParam("user_id") final long user_id, @RequestParam("is_profile_created") final boolean is_profile_created) {
        final UserEntity userToUpdate = userService.findUserById(user_id);
        userToUpdate.setProfileCreated(is_profile_created);

        userService.save(userToUpdate);

        return userToUpdate;
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