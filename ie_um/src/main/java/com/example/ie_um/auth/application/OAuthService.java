package com.example.ie_um.auth.application;

import com.example.ie_um.member.domain.Gender;
import com.example.ie_um.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.example.ie_um.auth.userInfo.OAuthUserInfo;
import com.example.ie_um.auth.userInfo.UserInfo;
import com.example.ie_um.auth.client.KakaoOAuthClient;
import com.example.ie_um.auth.exception.OAuthLoginFailedException;
import com.example.ie_um.auth.exception.UnsupportedProviderException;
import com.example.ie_um.global.jwt.JwtProvider;
import com.example.ie_um.global.jwt.dto.TokenDto;
import com.example.ie_um.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final KakaoOAuthClient kakaoOAuthClient;

    public String buildAuthUrl(String provider) {
        return switch (provider) {
            case "kakao" -> kakaoOAuthClient.getAuthUrl();
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        };
    }

    @Transactional
    public TokenDto handleOAuthLogin(String provider, String code) {
        String idToken = getIdToken(provider, code);
        UserInfo claims = parserIdTokne(idToken);
        OAuthUserInfo userInfo = getUserInfo(provider, claims);
        Member member = getOrCreateMember(userInfo, provider);
        String token = jwtProvider.createToken(member.getEmail(), member.getId());
        return new TokenDto(token);
    }

    private String getIdToken(String provider, String code) {
        try {
            return switch (provider) {
                case "kakao" -> kakaoOAuthClient.getIdToken(code);
                default -> throw new UnsupportedProviderException(provider);
            };
        } catch (Exception e) {
            throw new OAuthLoginFailedException(e.getMessage());
        }
    }

    private UserInfo parserIdTokne(String idToken) {
        try {
            return jwtProvider.parserIdToken(idToken);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("ID Token 파싱 실패: " + e.getMessage());
        }
    }

    private OAuthUserInfo getUserInfo(String provider, UserInfo claims) {
        try {
            return OAuthUserInfo.OAuthUserInfoFactory.getUserInfo(provider, claims);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("UserInfo 생성 실패: " + e.getMessage());
        }
    }

    private Member getOrCreateMember(OAuthUserInfo userInfo, String provider) {
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
        System.out.println("저장된 member: " + member.getId());
        return member;
    }
}
