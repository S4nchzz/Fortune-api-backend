package com.fortune_api.db.services;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByIdentityDocument(final String identityDocument) {
        return userRepository.findUserByIdentityDocument(identityDocument);
    }

    public UserEntity register(final String identityDocument, final String email, final String salt, final String password) {
        return userRepository.save(new UserEntity(identityDocument, email, salt, password));
    }

    public String findHashedPasswordByIdentityDocument(final String identityDocument) {
        return userRepository.findHashedPasswordByIdentityDocument(identityDocument);
    }

    public UserEntity findUserById(long userId) {
        return userRepository.findByUserId(userId);
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}