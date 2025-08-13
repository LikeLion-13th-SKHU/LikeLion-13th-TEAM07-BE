package com.example.ie_um.domain.user.service;

import com.example.ie_um.domain.user.dto.OAuth2Attributes;
import com.example.ie_um.domain.user.entity.LoginType;
import com.example.ie_um.domain.user.entity.Member;
import com.example.ie_um.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginType = LoginType.valueOf(registrationId.toUpperCase());
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 1. 소셜 로그인 사용자 정보 추출 및 공통 DTO로 변환
        OAuth2Attributes attributes = OAuth2Attributes.of(loginType, userNameAttributeName, oAuth2User.getAttributes());

        // 2. DB에 사용자 정보 저장 또는 업데이트
        Member member = saveOrUpdate(attributes, loginType);

        return oAuth2User; // 처리된 사용자 정보 반환
    }

    private Member saveOrUpdate(OAuth2Attributes attributes, LoginType loginType) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getNickName(), attributes.getGender()))

