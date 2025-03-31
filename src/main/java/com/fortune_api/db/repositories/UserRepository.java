package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u.password from f_user u where u.identity_document = ?1")
    String findHashedPasswordByIdentityDocument(String identity_document);

    @Query("select u from f_user u where u.id = ?1")
    UserEntity findByUserId(long userId);

    @Query("select u from f_user u where u.identity_document = ?1")
    UserEntity findUserByIdentityDocument(String identity_document);

    @Query("select u from f_user u where u.email = ?1")
    UserEntity findUserByEmail(String email);
}