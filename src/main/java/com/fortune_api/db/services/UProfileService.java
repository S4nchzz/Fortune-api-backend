package com.fortune_api.db.services;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.repositories.UProfileRepository;
import com.fortune_api.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UProfileService {
    @Autowired
    private UProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public UserProfileEntity generateUserProfile(final long user_id, final String name, final String address, final String phone, final boolean online) {
        final UserEntity user = userRepository.findById(user_id).get();
        final UserProfileEntity userProfileEntity = new UserProfileEntity(user, name, address, phone, online);

        return userProfileRepository.save(userProfileEntity);
    }

    public UserProfileEntity findById(long id) {
        return userProfileRepository.findById(id).get();
    }
}
