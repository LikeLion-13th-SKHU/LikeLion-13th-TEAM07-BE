package com.example.ie_um.global.config.jwt;

import com.example.ie_um.global.config.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);

        // --- 기존 리다이렉트 로직 주석 처리 ---
        /*
        // 2. 토큰을 포함한 리다이렉트 URL 생성
        String redirectUrl = UriComponentsBuilder.fromUriString("https://ieum-api.duckdns.org")
                .queryParam("token", token)
                .build().toUriString();

        // 3. 클라이언트로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        */

        // --- 테스트용: 토큰을 브라우저에 직접 출력하는 로직 추가 ---
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("## 로그인 성공! 발급된 JWT 토큰 ##\n\n" + token);
    }
}
