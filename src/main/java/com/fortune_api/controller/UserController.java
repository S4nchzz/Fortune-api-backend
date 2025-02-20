package com.fortune_api.controller;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.enums.IdentityDocument;
import com.fortune_api.db.services.AuthService;
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
    private AuthService userService;

    @Autowired
    private UProfileService userProfileService;

    @GetMapping("/login")
    public String login(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "password") final String password) {
        final String hashedPassword = userService.findHashedPasswordByIdentityDocument(identityDocument);
        if (hashedPassword == null) {
            Log.getInstance().writeLog("AuthController | hashedPassword is null");
            return null;
        }

        final boolean isPasswordCorrect = BCrypt.checkpw(password, hashedPassword);

        if (!isPasswordCorrect) {
            return null;
        }

        JSONObject loginResponse = new JSONObject();
        UserEntity user = userService.findUserByIdentityDocument(identityDocument);

        if (user != null) {
            
            if (user.getDni() != null) {
                loginResponse.put("identityDocument", user.getDni());
            } else if (user.getNie() != null) {
                loginResponse.put("identityDocument", user.getNie());
            } else {
                Log.getInstance().writeLog("AuthController | Dni and Nie are both null on findUser() data returned");
            }

            return loginResponse
                    .put("id", user.getId())
                    .put("email", user.getEmail())
                    .put("digital_sign", user.getDigital_sign()).toString();
        }

        return null;
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        final String salt = BCrypt.gensalt();
        final String passwordHashed = BCrypt.hashpw(password, salt);

        String dni = null;
        String nie = null;

        if (documentType(identityDocument) != null && documentType(identityDocument) == IdentityDocument.DNI) {
            dni = identityDocument;
        } else if (documentType(identityDocument) != null) {
            nie = identityDocument;
        } else {
            Log.getInstance().writeLog("AuthController | documentType(String) returned null");
            return null;
        }

        UserEntity user = userService.register(dni, nie, email, salt, passwordHashed);

        if (user == null) {
            return null;
        }

        JSONObject registerResponse = new JSONObject();
        registerResponse
                .put("user_id", user.getId())
                .put("email", user.getEmail())
                .put("digital_sign", user.getDigital_sign());

        if (documentType(identityDocument) != null && documentType(identityDocument) == IdentityDocument.DNI) {
            registerResponse.put("identityDocument", user.getDni());
        } else if (documentType(identityDocument) != null) {
            registerResponse.put("identityDocument", user.getNie());
        } else {
            Log.getInstance().writeLog("AuthController | documentType(String) returned null");
            return null;
        }

        return registerResponse.toString();
    }

    @PostMapping("/createDigitalSign")
    public String createDigitalSign(@RequestParam(name = "user_id") final long user_id, @RequestParam(name = "digital_sign") final int pin) {
        UserEntity user = userService.findUserById(user_id);

        if (user.getDigital_sign() != null) {
            return ""; // For some reason the user has already a digital sign
        }

        JSONObject createDigitalSign = new JSONObject();

        user.setDigital_sign(pin);
        userService.save(user);

        if (user.getDni() != null) {
            createDigitalSign.put("identityDocument", user.getDni());
        } else if (user.getNie() != null) {
            createDigitalSign.put("identityDocument", user.getNie());
        } else {
            Log.getInstance().writeLog("AuthController | Dni and Nie are both null on findUser() data returned");
        }

        return createDigitalSign
                .put("user_id", user.getId())
                .put("email", user.getEmail())
                .put("digital_sign", user.getDigital_sign()).toString();
    }

    public static IdentityDocument documentType(final String identityDocument) {
        if (identityDocument == null || identityDocument.isEmpty()) {
            return null;
        }

        int letterCount = 0;
        int nummberCount = 0;
        for (char c : identityDocument.toCharArray()) {
            if (c >= 65 && c <= 90) {
                letterCount++;
            }

            if (c >= 48 && c <= 57) {
                nummberCount++;
            }
        }

        return Character.isAlphabetic(identityDocument.charAt(identityDocument.length() - 1)) && letterCount == 1 && nummberCount == 8 && identityDocument.length() == 9 ? IdentityDocument.DNI : IdentityDocument.NIE;
    }
}