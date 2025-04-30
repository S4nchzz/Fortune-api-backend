package com.fortune_api.db.entities.bank_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "f_card_data")
@Table(name = "f_card_data")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(name = "card_uuid", nullable = false)
    @JsonProperty("card_uuid")
    private String card_uuid;

    @Column(name = "card_type", nullable = false)
    @JsonProperty("card_type")
    private String cardType;

    @Column(name = "card_number", nullable = false, unique = true)
    @JsonProperty("card_number")
    private String cardNumber;

    @Column(name = "exp_date", nullable = false)
    @JsonProperty("exp_date")
    private String expDate;

    @Column(name = "cvv", nullable = false)
    @JsonProperty("cvv")
    private int cvv;

    @Column(name = "pin", nullable = false)
    @JsonProperty("pin")
    private int pin;

    @Column(name = "balance", nullable = false)
    @JsonProperty("balance")
    private double balance;

    @Column(name = "blocked", nullable = false)
    @JsonProperty("blocked")
    private boolean blocked;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private AccountEntity account;

    @OneToMany(mappedBy = "card_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovementCardEntity> movements = new HashSet<>();

    public CardEntity(final String uuid, final String type, final String cardNumber, final String expDate, int cvv, int pin, final double balance, final boolean blocked, final AccountEntity account) {
        this.card_uuid = uuid;
        this.cardType = type;
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvv = cvv;
        this.pin = pin;
        this.balance = balance;
        this.blocked = blocked;

        this.account = account;
    }
}