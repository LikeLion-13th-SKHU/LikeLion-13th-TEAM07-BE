package com.example.ie_um.member.domain;

import jakarta.persistence.*;
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

    @Column(name = "nickName", nullable = false, unique = true)
    private String nickName;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Builder
    private Member(String email, String name, String nickName,  Gender gender) {
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        this.age = 0;
    }

    public void update(String nickName, Gender gender, Integer age) {
        this.nickName = nickName;
        this.gender = gender;
        this.age = age;
    }
}
