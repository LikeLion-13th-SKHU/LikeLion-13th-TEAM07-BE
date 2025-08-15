package com.example.ie_um.global.config;

import com.example.ie_um.domain.user.service.CustomOAuth2UserService;
import com.example.ie_um.global.config.jwt.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // 👈 OAuth2SuccessHandler 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API이므로 CSRF 보호 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/login/oauth2/success").permitAll() // 성공 리다이렉트 URL 허용
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 허용
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 로그인 페이지 URL (프론트엔드에서 구현)
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉트될 기본 URL
                        .successHandler(oAuth2SuccessHandler) // 👈 OAuth2SuccessHandler 등록
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 소셜 로그인 성공 후 사용자 정보 처리 로직을 연결
                        )
                );
        return http.build();
    }
}
