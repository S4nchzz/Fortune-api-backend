package com.fortune_api.db.entities.bank_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fortune_api.db.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "f_account")
@Table(name = "f_account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_id", nullable = false)
    @JsonProperty("account_id")
    private String account_id;

    @OneToOne
    @JoinColumn(name = "proprietary")
    private UserEntity proprietary;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardEntity> cards = new HashSet<>();

    public AccountEntity(String accountUUID, UserEntity proprietary, double balance) {
        this.account_id = accountUUID;
        this.proprietary = proprietary;

        this.cards = new HashSet<>();
    }
}
