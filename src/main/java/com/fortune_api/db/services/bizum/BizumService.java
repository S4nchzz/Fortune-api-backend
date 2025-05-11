package com.fortune_api.db.services.bizum;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bizum.BizumEntity;
import com.fortune_api.db.repositories.BizumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizumService {
    @Autowired
    private BizumRepository bizumRepository;

    public BizumEntity saveOperation(UserEntity user, UserEntity userToSend, String description, double amount, boolean isRequesting) {
        return bizumRepository.save(new BizumEntity(
                user,
                userToSend,
                description,
                amount,
                isRequesting
        ));
    }

    public List<BizumEntity> getBizums(long id) {
        return bizumRepository.getBizums(id);
    }
}