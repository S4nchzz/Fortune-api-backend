package com.fortune_api.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private long id;

    @Column(name = "identity_document", unique = true)
    @JsonProperty("identity_document")
    private String identity_document;

    @Column(name = "email", nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "salt", nullable = false)
    @JsonIgnore
    private String salt;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "digital_sign")
    @JsonProperty("digital_sign")
    private Integer digital_sign;

    public UserEntity(final String identity_document, final String email, final String salt, final String password) {
        this.identity_document = identity_document;
        this.email = email;
        this.salt = salt;
        this.password = password;
    }
}