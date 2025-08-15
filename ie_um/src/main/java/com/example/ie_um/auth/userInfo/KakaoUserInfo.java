package com.example.ie_um.auth.userInfo;

public class KakaoUserInfo implements OAuthUserInfo {
    private final UserInfo attributes;

    public KakaoUserInfo(UserInfo attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        return attributes.email();
    }

    @Override
    public String getName() {
        return attributes.nickname();
    }
}
