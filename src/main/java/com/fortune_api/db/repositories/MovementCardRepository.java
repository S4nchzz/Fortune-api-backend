package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementCardRepository extends JpaRepository<MovementCardEntity, Integer> {
}
