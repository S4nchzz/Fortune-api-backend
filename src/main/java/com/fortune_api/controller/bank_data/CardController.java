package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.services.bank_data.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/b_operations/card")
public class CardController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/findAccMainCard")
    public CardEntity getMainCard(@RequestParam("accountId") final String accountId) {
        AccountEntity account = accountService.findAccountByAccId(accountId);

        return account.getCards().stream()
                .filter(card -> "MAIN".equals(card.getCardType()))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/findAccCards")
    public List<CardEntity> findAllCards(@RequestParam("accountId") final String accountId) {
        AccountEntity account = accountService.findAccountByAccId(accountId);
        return account.getCards().stream().toList();
    }
}
