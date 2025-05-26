package com.fortune_api.db.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity(name = "f_user_profile")
@Table(name = "f_user_profile")
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "online")
    @JsonProperty("online")
    private boolean online;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "address")
    @JsonProperty("address")
    private String address;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "pfp", columnDefinition = "LONGTEXT")
    @JsonProperty("pfp")
    private String pfp;

    public UserProfileEntity(UserEntity user, String name, String address, String phone, String pfp, Boolean online) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.online = online;
        this.pfp = pfp;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
