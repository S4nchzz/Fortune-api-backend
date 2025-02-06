package com.fortune_api.db.services;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity login(final String nif_nie, final String password) {
        return userRepository.findByNifNieAndPassword(nif_nie, password);
    }

    public UserEntity register(String nif_nie, String email, String password) {
        return userRepository.save(new UserEntity(nif_nie, email, password));
    }
}