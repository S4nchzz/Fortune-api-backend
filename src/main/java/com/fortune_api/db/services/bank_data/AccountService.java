package com.fortune_api.db.services.bank_data;

import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity saveAccount(AccountEntity account) {
        return accountRepository.save(account);
    }

    public AccountEntity findAccountByProprietary(final long userId) {
        return accountRepository.findAccountByPropietaryId(userId);
    }

    public AccountEntity findAccountByAccId(final String accountId) {
        return accountRepository.findAccountByAccId(accountId);
    }
}
