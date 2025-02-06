package com.fortune_api.security;

import com.fortune_api.log.Log;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordComplexity {
    public static byte[] sha256(final String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            Log.getInstance().writeLog("PasswordComplexity | Digest algorithm unsuported");
            return null;
        }
    }

    public static String saltGenerator() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            sb.append((char) (33 + (int) (Math.random() * 94)));
        }

        return sb.toString();
    }
}
