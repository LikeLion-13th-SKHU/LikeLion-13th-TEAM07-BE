package com.example.ie_um.domain.member.service;

import com.example.ie_um.domain.member.dto.OAuth2Attributes;
import com.example.ie_um.domain.member.entity.Gender;
import com.example.ie_um.domain.member.entity.LoginType;
import com.example.ie_um.domain.member.entity.Member;
import com.example.ie_um.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

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

        // 3. UserDetails 객체로 변환하여 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }

    private Member saveOrUpdate(OAuth2Attributes attributes, LoginType loginType) {
        Optional<Member> optionalMember = memberRepository.findByEmail(attributes.getEmail());
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            Gender gender = Gender.valueOf(attributes.getGender().toUpperCase());
            member.update(attributes.getName(), attributes.getNickName(), gender);
        } else {
            member = attributes.toEntity(loginType);
        }
        return memberRepository.save(member);
    }
}
