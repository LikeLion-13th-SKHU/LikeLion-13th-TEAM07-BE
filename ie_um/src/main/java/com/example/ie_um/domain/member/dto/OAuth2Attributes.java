package com.example.ie_um.domain.member.dto;

import com.example.ie_um.domain.member.entity.Gender;
import com.example.ie_um.domain.member.entity.LoginType;
import com.example.ie_um.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String nickName;
    private final String gender;

    @Builder
    private OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey,
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
        String email = String.valueOf(response.get("email"));
        if ("null".equals(email)) {
            throw new IllegalArgumentException("네이버로부터 이메일 정보를 받아오지 못했습니다. 동의 항목을 확인해주세요.");
        }
        return OAuth2Attributes.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .nickName(String.valueOf(response.get("nickname")))
                .gender("UNKNOWN")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = String.valueOf(kakaoAccount.get("email"));
        if ("null".equals(email)) {
            throw new IllegalArgumentException("네이버로부터 이메일 정보를 받아오지 못했습니다. 동의 항목을 확인해주세요.");
        }
        return OAuth2Attributes.builder()
                .name(String.valueOf(profile.get("nickname"))) // 카카오는 이름 정보가 없어 닉네임을 이름으로 사용
                .email(String.valueOf(kakaoAccount.get("email")))
                .nickName(String.valueOf(profile.get("nickname")))
                .gender("UNKNOWN")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 소셜 로그인 타입에 따라 적절한 파싱 메서드를 호출하는 팩토리 메서드
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
        Gender genderEnum = Gender.valueOf(this.gender.toUpperCase());
        return Member.builder()
                .name(name)
                .email(email)
                .nickName(nickName)
                .gender(genderEnum)
                .password("social-login") // 소셜 로그인 사용자는 비밀번호가 의미 없으므로 임의 값 설정
                .loginType(loginType)
                .build();
    }
}
