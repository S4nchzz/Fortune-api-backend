package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from f_user u where u.nif_nie = ?1 AND u.password = ?2")
    UserEntity findByNifNieAndPassword(final String nif_nie, final String password);
}