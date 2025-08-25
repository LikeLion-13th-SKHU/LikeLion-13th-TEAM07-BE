package com.example.ie_um.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.ie_um.auth.application.OAuthService;
import com.example.ie_um.global.jwt.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "OAuth 로그인 관련 API")
@RequestMapping("/api/oauth2")
public class OAuthController {

    private final OAuthService oAuthService;

    @Operation(
            summary = "카카오 로그인 요청",
            description = "사용자를 카카오 로그인 인증 페이지로 리다이렉트합니다."
    )
    @GetMapping("/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/oauth2/authorization/kakao");
    }

    @Operation(
            summary = "카카오 인증 URL 생성",
            description = "카카오 인증 페이지로 사용자를 리다이렉트합니다."
    )
    @GetMapping("/authorization/kakao")
    public void redirectToAuthorize(HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.buildAuthUrl();
        response.sendRedirect(redirectUrl);
    }

    @Operation(
            summary = "카카오 OAuth 콜백 처리 및 JWT 반환",
            description = "카카오에서 받은 인가 코드로 토큰을 발급하고 사용자에게 JWT 반환"
    )

    @GetMapping("/callback/kakao")
    public ResponseEntity<TokenDto> handleCallback(@RequestParam String code) {
        TokenDto tokenDto = oAuthService.handleOAuthLogin(code);
        return ResponseEntity.ok(tokenDto);
    }
}
