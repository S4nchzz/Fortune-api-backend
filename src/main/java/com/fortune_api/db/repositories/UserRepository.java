package com.fortune_api.db.repositories;

import com.fortune_api.db.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u from f_user u where u.dni = ?1 AND u.password = ?2")
    UserEntity findUserByDniAndPassword(final String dni, final byte [] password);

    @Query("select u from f_user u where u.dni = ?1 AND u.password = ?2")
    UserEntity findUserByNieAndPassword(final String nie, final byte [] password);

    @Query("select u.salt from f_user u where u.dni = ?1")
    String findSaltByDni(final String dni);

    @Query("select u.salt from f_user u where u.nie = ?1")
    String findSaltByNie(final String nie);
}