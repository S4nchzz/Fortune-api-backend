package com.fortune_api.db.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity(name = "f_userProfile")
@Table(name = "f_userProfile")
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "online")
    private boolean online;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "telf")
    private String telf;

    @Column(name = "pfp")
    private byte [] pfp;

    public UserProfileEntity(UserEntity user_id, String name, String address, String telf) {
        this.user = user_id;
        this.name = name;
        this.address = address;
        this.telf = telf;
    }
}
