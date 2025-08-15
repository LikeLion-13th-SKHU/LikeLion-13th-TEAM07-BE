package com.example.ie_um.auth.userInfo;

import java.util.Map;

public interface OAuthUserInfo {
    String getEmail();
    String getName();

    class OAuthUserInfoFactory {
        public static OAuthUserInfo getUserInfo(String provider, UserInfo attributes) {
            return switch (provider) {
                case "kakao" -> new KakaoUserInfo(attributes);
                default -> throw new IllegalArgumentException("Unknown provider: " + provider);
            };
        }
    }
}
