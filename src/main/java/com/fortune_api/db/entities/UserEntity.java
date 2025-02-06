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

    @Column(name = "nif_nie", nullable = false, unique = true)
    private String nif_nie;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "digital_sign")
    private Integer digital_sign;

    public UserEntity(String nif_nie, String email, String password) {
        this.nif_nie = nif_nie;
        this.email = email;
        this.password = password;
    }
}