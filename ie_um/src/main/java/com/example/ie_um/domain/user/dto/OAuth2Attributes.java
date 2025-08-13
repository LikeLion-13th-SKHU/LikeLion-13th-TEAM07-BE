package com.example.ie_um.domain.user.dto;

import com.example.ie_um.domain.user.entity.LoginType;
import com.example.ie_um.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String nickName;
    private String gender;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey,
                            String name, String email, String nickName, String gender) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.gender = gender;
    }

    public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attributes.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .nickName(String.valueOf(response.get("nickname")))
                .gender(String.valueOf(response.get("gender")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return OAuth2Attributes.builder()
                .name(String.valueOf(profile.get("nickname"))) // 카카오는 이름 정보가 없어 닉네임을 이름으로 사용
                .email(String.valueOf(kakaoAccount.get("email")))
                .nickName(String.valueOf(profile.get("nickname")))
                .gender(String.valueOf(kakaoAccount.get("gender")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuth2Attributes of(LoginType loginType, String userNameAttributeName, Map<String, Object> attributes) {
        if (loginType == LoginType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (loginType == LoginType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return null;
    }

    public Member toEntity(LoginType loginType) {
        return Member.builder()
                .name(name)
                .email(email)
                .nickName(nickName)
                .gender(gender)
                .password("social-login") // 소셜 로그인 사용자는 비밀번호가 의미 없으므로 임의 값 설정
                .loginType(loginType)
                .build();
    }
}
