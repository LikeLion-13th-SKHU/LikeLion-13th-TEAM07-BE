package com.example.ie_um.member.domain;

import lombok.Getter;

@Getter
public enum Gender {
    FEMALE("여성"),
    MALE("남성");

    private final String description;

    Gender(String description) {
        this.description = description;
    }
}
