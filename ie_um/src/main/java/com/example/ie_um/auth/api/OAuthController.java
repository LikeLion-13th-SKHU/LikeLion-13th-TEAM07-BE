package com.example.ie_um.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.ie_um.auth.application.OAuthService;
import com.example.ie_um.global.jwt.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "OAuth 로그인 관련 API")
@RequestMapping("/api/oauth2")
public class OAuthController {
    private final OAuthService oAuthService;

    // 카카오 로그인 요청이 들어오면, '/api/oauth2/authorization/kakao'라는 주소로 보냄
    private final Map<String, String> redirectUris = Map.of(
            "kakao", "/api/oauth2/authorization/kakao"
    );

    @Operation(
            summary = "소셜 로그인 제공자 선택",
            description = "사용자가 로그인하려는 소셜 로그인 제공자(카카오)를 선택하고 해당 제공자에게 리다이렉트합니다."
    )
    @GetMapping("/login")
    public void redirectToProvider(@RequestParam("provider") String provider, HttpServletResponse response) throws IOException {
        // "?provider=kakao"처럼 주소 뒤에 따라오는 'kakao'라는 글자를 받음
        String redirectUri = redirectUris.get(provider.toLowerCase());

        if (redirectUri == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지 않는 소셜 로그인입니다.");
            return;
        }

        response.sendRedirect(redirectUri);
    }

    @Operation(
            summary = "소셜 로그인 인증 URL 생성",
            description = "카카오에 따라 인증 URL을 생성하고, 사용자를 해당 인증 페이지로 리다이렉트합니다."
    )
    @GetMapping("/authorization/{provider}")
    public void redirectToAuthorize(
            // 주소의 {provider} 부분, 즉 'kakao'라는 글자를 받아옴
            @PathVariable String provider,
            HttpServletResponse response
    ) throws IOException {
        // 카카오에게 로그인할 수 있는 페이지 주소를 알려달라고 요청
        String redirectUrl = oAuthService.buildAuthUrl(provider);
        // 카카오가 알려준 주소로 사용자를 보냄
        response.sendRedirect(redirectUrl);
    }
    
    @Operation(
            summary = "OAuth 콜백 처리 및 JWT 토큰 반환",
            description = "카카오에서 받은 인가 코드로 ID 토큰을 요청하고, 사용자 정보를 처리하여 JWT 토큰을 반환합니다."
    )
    // 카카오 로그인이 성공하면, 카카오가 이 주소로 사용자를 다시 보내줌
    @GetMapping("/callback/{provider}")
    public ResponseEntity<TokenDto> handleCallback(
            @PathVariable String provider,
            // 카카오가 비밀번호 같은 인가 코드를 보냄
            @RequestParam String code
    ) {
        // 인가 코드를 oAuthService에게 넘겨주고 토큰을 발급
        TokenDto tokenDto = oAuthService.handleOAuthLogin(provider, code);

        // 사용자에게 토큰을 돌려줌
        return ResponseEntity.ok().body(tokenDto);
    }
}
