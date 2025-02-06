package com.fortune_api.db.services;

import com.fortune_api.controller.AuthController;
import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.enums.IdentityDocument;
import com.fortune_api.db.repositories.UserRepository;
import com.fortune_api.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByIdentityDocument(final String identityDocument) {
        if (AuthController.documentType(identityDocument) != null && AuthController.documentType(identityDocument) == IdentityDocument.DNI) {
            return userRepository.findUserByDniAndPassword(identityDocument);
        } else if (AuthController.documentType(identityDocument) != null) {
            return userRepository.findUserByNieAndPassword(identityDocument);
        } else {
            Log.getInstance().writeLog("AuthService | AuthController.documentType(String) returned null");
            return null;
        }
    }

    public UserEntity register(final String dni, final String nie, final String email, final String salt, final String password) {
        return userRepository.save(new UserEntity(dni, nie, email, salt, password));
    }

    public String findHashedPasswordByIdentityDocument(final String identityDocument) {
        if (AuthController.documentType(identityDocument) != null && AuthController.documentType(identityDocument) == IdentityDocument.DNI) {
            return userRepository.findHashedPasswordByDni(identityDocument);
        } else if (AuthController.documentType(identityDocument) != null) {
            return userRepository.findHashedPasswordByNie(identityDocument);
        } else {
            Log.getInstance().writeLog("AuthService | AuthController.documentType(String) returned null");
            return null;
        }
    }

    public String findSaltByIdentityDocument(final String identityDocument) {
        if (AuthController.documentType(identityDocument) != null && AuthController.documentType(identityDocument) == IdentityDocument.DNI) {
            return userRepository.findSaltByDni(identityDocument);
        } else if (AuthController.documentType(identityDocument) != null) {
            return userRepository.findSaltByNie(identityDocument);
        } else {
            Log.getInstance().writeLog("AuthService | AuthController.documentType(String) returned null");
            return null;
        }
    }
}