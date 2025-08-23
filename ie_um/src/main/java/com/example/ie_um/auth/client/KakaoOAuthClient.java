package com.example.ie_um.auth.client;

import com.example.ie_um.auth.exception.OAuthLoginFailedException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
    private final WebClient webClient;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;
    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    public String getAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "&response_type=code&" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=openid,profile_nickname,profile_image,account_email";
    }

    public String getIdToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");

        Map<String, Object> response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        if (response == null || response.containsKey("error")) {
            String errorDescription =
                    response != null ? response.getOrDefault("error_description", "No description provided.").toString()
                            : "Response is null.";
            throw new OAuthLoginFailedException("카카오 토큰 발급에 실패했습니다. 원인: " + errorDescription);
        }

        if (!response.containsKey("id_token")) {
            throw new OAuthLoginFailedException("카카오 응답에 id_token이 없습니다. openid scope를 요청했는지 확인해주세요. 응답: " + response);
        }

        return response.get("id_token").toString();
    }
}
