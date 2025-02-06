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

    @Column(name = "dni", nullable = true, unique = true)
    private String dni;

    @Column(name = "nie", nullable = true, unique = true)
    private String nie;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "password", nullable = false)
    private byte [] password;

    @Column(name = "digital_sign", nullable = true)
    private Integer digital_sign;

    public UserEntity(final String dni, final String nie, final String email, final String salt, final byte [] password) {
        this.dni = dni;
        this.nie = nie;
        this.email = email;
        this.salt = salt;
        this.password = password;
    }
}