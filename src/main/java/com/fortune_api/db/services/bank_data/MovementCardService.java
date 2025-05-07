package com.fortune_api.db.services.bank_data;

import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import com.fortune_api.db.repositories.MovementCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovementCardService {
    @Autowired
    private MovementCardRepository movementCardRepository;

    public MovementCardEntity saveMovement(String amount, String receptor_entity, String sender, CardEntity card) {
        return movementCardRepository.save(new MovementCardEntity(amount, receptor_entity, sender, card));
    }
}
