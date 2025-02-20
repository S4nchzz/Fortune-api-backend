package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UProfileRepository extends JpaRepository<UserProfileEntity, Long> {
}
