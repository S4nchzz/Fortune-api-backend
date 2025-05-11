package com.fortune_api.db.services.bank_data;

import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public CardEntity saveCard(CardEntity card) {
        return cardRepository.save(card);
    }
}
