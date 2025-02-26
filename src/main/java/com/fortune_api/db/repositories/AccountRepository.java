package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.bank_data.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    @Query("select a from f_account a where a.proprietary.id = ?1")
    AccountEntity findAccountByPropietaryId(long userId);

    @Query("select a from f_account a where a.account_id = ?1")
    AccountEntity findAccountByAccId(final String accountId);
}
