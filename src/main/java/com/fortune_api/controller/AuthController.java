package com.fortune_api.controller;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.services.AuthService;
import com.fortune_api.security.PasswordComplexity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private AuthService userService;

    @GetMapping("/login")
    public String login(@RequestParam(name = "dni_nie") final String dni_nie, @RequestParam(name = "password") final String password) {
        String salt = null;
        if (isDocumentDni(dni_nie)) {
            salt = userService.findSaltByDni(dni_nie);
        } else {
            salt = userService.findSaltByNie(dni_nie);
        }

        JSONObject loginResponse = new JSONObject();

        if (salt != null) {
            final String salt_password = salt.concat(password);
            byte [] hashedPassword = PasswordComplexity.sha256(salt_password);

            UserEntity user = userService.login(dni_nie, hashedPassword);

            if (user != null) {
                if (user.getDni() != null) {
                    loginResponse.put("dni_nie", user.getDni());
                } else {
                    loginResponse.put("dni_nie", user.getNie());
                }
                return loginResponse
                        .put("id", user.getId())
                        .put("email", user.getEmail())
                        .put("digital_sign", user.getDigital_sign()).toString();
            }
        }


        return null;
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "dni_nie") final String dni_nie, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        final String salt = PasswordComplexity.saltGenerator();
        final String salt_password = salt + password;

        String dni = null;
        String nie = null;

        if (isDocumentDni(dni_nie)) {
            dni = dni_nie;
        } else {
            nie = dni_nie;
        }

        UserEntity user = userService.register(dni, nie, email, salt, PasswordComplexity.sha256(salt_password));

        JSONObject registerResponse = new JSONObject();

        if (user != null) {
            if (isDocumentDni(dni_nie)) {
                registerResponse.put("dni", user.getDni());
            } else {
                registerResponse.put("nie", user.getNie());
            }

            return registerResponse
                    .put("id", user.getId())
                    .put("email", user.getEmail())
                    .put("digital_sign", user.getDigital_sign()).toString();
            }

        return null;
    }

    public static boolean isDocumentDni(final String dni_nie) {
        if (dni_nie == null || dni_nie.isEmpty()) {
            return false;
        }

        int letterCount = 0;
        int nummberCount = 0;
        for (char c : dni_nie.toCharArray()) {
            if (c >= 65 && c <= 90) {
                letterCount++;
            }

            if (c >= 48 && c <= 57) {
                nummberCount++;
            }
        }

        return Character.isAlphabetic(dni_nie.charAt(dni_nie.length() - 1)) && letterCount == 1 && nummberCount == 8 && dni_nie.length() == 9;
    }
}