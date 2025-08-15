package com.example.ie_um.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String gender;

    @Builder
    public Member(String email, String password, String name, LoginType loginType, String nickName, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.loginType = loginType;
        this.nickName = nickName;
        this.gender = gender;
    }

    public Member update(String name, String nickName, String gender) {
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        return this;
    }
}
