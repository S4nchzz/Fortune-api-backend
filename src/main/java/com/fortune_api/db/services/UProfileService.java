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

    public UserProfileEntity createUserProfile(final long user_id, final String name, final String address, final String phone, String pfp, final boolean online) {
        final UserEntity user = userRepository.findById(user_id).get();
        final UserProfileEntity userProfileEntity = new UserProfileEntity(user, name, address, phone, pfp, online);

        return userProfileRepository.save(userProfileEntity);
    }

    public UserProfileEntity findById(long id) {
        return userProfileRepository.findById(id).get();
    }

    public UserProfileEntity findProfileByUserId(long userId) {
        return userProfileRepository.findProfileByUserId(userId);
    }

    public UserProfileEntity findByPhone(String phone) {
        return userProfileRepository.findByPhone(phone);
    }

    public UserProfileEntity save(UserProfileEntity userProfileEntity) {
        return userProfileRepository.save(userProfileEntity);
    }
}
