package com.fortune_api.db.services;

import com.fortune_api.controller.UserController;
import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.enums.IdentityDocument;
import com.fortune_api.db.repositories.UserRepository;
import com.fortune_api.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByIdentityDocument(final String identityDocument) {
        if (UserController.documentType(identityDocument) != null && UserController.documentType(identityDocument) == IdentityDocument.DNI) {
            return userRepository.findUserByDniAndPassword(identityDocument);
        } else if (UserController.documentType(identityDocument) != null) {
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
        if (UserController.documentType(identityDocument) != null && UserController.documentType(identityDocument) == IdentityDocument.DNI) {
            return userRepository.findHashedPasswordByDni(identityDocument);
        } else if (UserController.documentType(identityDocument) != null) {
            return userRepository.findHashedPasswordByNie(identityDocument);
        } else {
            Log.getInstance().writeLog("AuthService | AuthController.documentType(String) returned null");
            return null;
        }
    }

    public UserEntity findUserById(long userId) {
        return userRepository.findById(userId).get();
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}