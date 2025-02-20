package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    @Query("select up from f_user_profile up where up.user.id = ?1")
    UserProfileEntity findProfileByUserId(long userId);
}
