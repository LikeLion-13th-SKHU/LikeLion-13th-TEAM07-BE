package com.example.ie_um.auth.client;

import com.example.ie_um.auth.api.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
    private final WebClient webClient;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.client-secret}") // yml 파일에 client-secret 추가
    private String clientSecret;
    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    public String getAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }

    public String getIdToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        params.add("client_secret", clientSecret);

        KakaoTokenResponse response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class) // ◀ DTO 클래스로 바로 매핑
                .block();

        if (response == null || response.idToken() == null) {
            throw new IllegalArgumentException("Kakao ID 토큰을 가져오지 못했습니다.");
        }

        return response.idToken();
    }
}
