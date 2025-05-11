package com.fortune_api.db.entities.bizum;

import com.fortune_api.db.entities.UserEntity;
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
@Entity(name = "f_bizum")
@Table(name = "f_bizum")
public class BizumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "_from", referencedColumnName = "id")
    private UserEntity from;

    @ManyToOne
    @JoinColumn(name = "_to", referencedColumnName = "id")
    private UserEntity to;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date")
    private Date date;

    @Column(name = "is_requesting")
    private boolean isRequesting;

    public BizumEntity(UserEntity from, UserEntity to, String description, double amount, boolean isRequesting) {
        this.from = from;
        this.to = to;
        this.description = description;
        this.amount = amount;
        this.date = new Date();
        this.isRequesting = isRequesting;
    }
}
