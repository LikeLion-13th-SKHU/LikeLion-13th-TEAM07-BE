package com.example.ie_um.auth.application;

import com.example.ie_um.member.domain.Gender;
import com.example.ie_um.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.example.ie_um.auth.userInfo.OAuthUserInfo;
import com.example.ie_um.auth.userInfo.UserInfo;
import com.example.ie_um.auth.client.KakaoOAuthClient;
import com.example.ie_um.auth.exception.OAuthLoginFailedException;
import com.example.ie_um.global.jwt.JwtProvider;
import com.example.ie_um.global.jwt.dto.TokenDto;
import com.example.ie_um.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final KakaoOAuthClient kakaoOAuthClient;

    public String buildAuthUrl() {
        return kakaoOAuthClient.getAuthUrl();
    }

    @Transactional
    public TokenDto handleOAuthLogin(String code) {
        String idToken = getIdToken(code);
        UserInfo claims = parseIdToken(idToken);
        OAuthUserInfo userInfo = getUserInfo(claims);
        Member member = getOrCreateMember(userInfo);
        String token = jwtProvider.createToken(member.getEmail(), member.getId()); // 성공
        return new TokenDto(token);
    }

    private String getIdToken(String code) {
        try {
            return kakaoOAuthClient.getIdToken(code);
        } catch (Exception e) {
            throw new OAuthLoginFailedException(e.getMessage());
        }
    }

    private UserInfo parseIdToken(String idToken) {
        try {
            return jwtProvider.parserIdToken(idToken);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("ID Token 파싱 실패: " + e.getMessage());
        }
    }

    private OAuthUserInfo getUserInfo(UserInfo claims) {
        try {
            return OAuthUserInfo.OAuthUserInfoFactory.getUserInfo("kakao", claims);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("UserInfo 생성 실패: " + e.getMessage());
        }
    }

    private Member getOrCreateMember(OAuthUserInfo userInfo) {
        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(userInfo.getEmail())
                                .name(userInfo.getName())
                                .nickName("")
                                .gender(Gender.UNKNOWN)
                                .build()
                ));

        memberRepository.flush();
        return member;
    }
}
