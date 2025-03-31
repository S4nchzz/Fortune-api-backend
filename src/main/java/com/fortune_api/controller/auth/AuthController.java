package com.fortune_api.controller.auth;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.services.UserService;
import com.fortune_api.log.Log;
import com.fortune_api.dto.AuthResponse;
import com.fortune_api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> register(@RequestParam(name = "identityDocument") final String identityDocument, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        final String salt = BCrypt.gensalt();
        final String passwordHashed = BCrypt.hashpw(password, salt);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}