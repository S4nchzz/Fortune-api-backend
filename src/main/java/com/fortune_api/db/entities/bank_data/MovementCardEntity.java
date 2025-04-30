package com.fortune_api.db.entities.bank_data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "f_card_movements")
@Table(name = "f_card_movements")
public class MovementCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private CardEntity card_id;

    @Column(name = "entity_receiver", nullable = false)
    private String entity_receiver;

    @Column(name = "entity_sender", nullable = false)
    private String entity_sender;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "amount", nullable = false)
    private String amount;
}
