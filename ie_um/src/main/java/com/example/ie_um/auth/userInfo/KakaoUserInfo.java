package com.example.ie_um.auth.userInfo;

public class KakaoUserInfo {
    private final UserInfo attributes;

    public KakaoUserInfo(UserInfo attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return attributes.email();
    }

    public String getName() {
        return attributes.nickname();
    }

    public String getPicture() {
        return attributes.picture();
    }
}