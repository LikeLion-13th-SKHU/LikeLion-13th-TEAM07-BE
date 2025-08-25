package com.example.ie_um.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickName", unique = true)
    private String nickName;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Column(name = "profile_img")
    public String profileImg;

    @Builder
    private Member(String email, String name, String nickName, String profileImg) {
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.gender = null;
        this.age = null;
        this.profileImg = profileImg;
    }

    public void update(String nickName, Gender gender, Integer age) {
        this.nickName = nickName;
        this.gender = gender;
        this.age = age;
    }

    public void updateImage(String profileImg) {
        this.profileImg = profileImg;
    }
}
