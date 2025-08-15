package com.example.ie_um.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @Column(name = "nickName", nullable = false, unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Builder
    private Member(String email, String password, String name, LoginType loginType, String nickName, Gender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.loginType = loginType;
        this.nickName = nickName;
        this.gender = gender;
    }

    public void update(String name, String nickName, Gender gender) {
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
    }
}
