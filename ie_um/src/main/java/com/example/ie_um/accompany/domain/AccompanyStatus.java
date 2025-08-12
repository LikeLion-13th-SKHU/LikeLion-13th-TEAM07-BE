package com.example.ie_um.accompany.domain;

import lombok.Getter;

@Getter
public enum AccompanyStatus {
    PENDING("대기중"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨"),
    LEAVE("탈퇴함");

    private final String description;

    AccompanyStatus(String description) {
        this.description = description;
    }

}
