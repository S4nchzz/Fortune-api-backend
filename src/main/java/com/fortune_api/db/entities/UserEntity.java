package com.fortune_api.db.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "f_user")
@Table(name = "f_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "nie", nullable = true, unique = true)
    private String nie;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "digital_sign")
    private Integer digital_sign;

    @Column(name = "is_profile_created")
    private boolean isProfileCreated;

    public UserEntity(final String dni, final String nie, final String email, final String salt, final String password) {
        this.dni = dni;
        this.nie = nie;
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.isProfileCreated = false;
    }
}