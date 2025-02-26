package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.bank_data.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Integer> {

}
