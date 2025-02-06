package com.fortune_api.db.services;

import com.fortune_api.controller.AuthController;
import com.fortune_api.db.entities.UserEntity;
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

    public UserEntity login(final String dni_nie, final byte [] password) {
        if (AuthController.isDocumentDni(dni_nie)) {
            return userRepository.findUserByDniAndPassword(dni_nie, password);
        } else {
            return userRepository.findUserByNieAndPassword(dni_nie, password);
        }
    }

    public UserEntity register(final String dni, final String nie, final String email, final String salt, byte [] password) {
        return userRepository.save(new UserEntity(dni, nie, email, salt, password));
    }

    public String findSaltByDni(final String dni) {
        return userRepository.findSaltByDni(dni);
    }

    public String findSaltByNie(final String nie) {
        return userRepository.findSaltByNie(nie);
    }
}