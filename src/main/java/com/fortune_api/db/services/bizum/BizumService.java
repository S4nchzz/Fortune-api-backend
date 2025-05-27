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

    public BizumEntity saveOperation(UserEntity userToSend, UserEntity user, String description, double amount, boolean isRequesting) {
        return bizumRepository.save(new BizumEntity(
                userToSend,
                user,
                description,
                amount,
                isRequesting
        ));
    }

    public List<BizumEntity> getBizums(long id) {
        return bizumRepository.getBizums(id);
    }

    public List<BizumEntity> getNonRequestedBizums(long id) {
        return bizumRepository.getNonRequestedBizums(id);
    }

    public List<BizumEntity> getRequestedBizums(long id) {
        return bizumRepository.getRequestedBizums(id);
    }

    public void denyBizum(int bizumID) {
        bizumRepository.deleteById(bizumID);
    }

    public BizumEntity findBizumById(int bizumID) {
        return bizumRepository.findById(bizumID).orElse(null);
    }

    public void saveBizum(BizumEntity bizum) {
        bizumRepository.save(bizum);
    }

    public List<BizumEntity> getMoreThan3Bizums(long user_id) {
        return bizumRepository.getMoreThan3Bizums(user_id);
    }
}