package com.example.ie_um.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("expires_in") Integer expiresIn,
    @JsonProperty("id_token") String idToken, // openid 스코프 추가 시 포함됨
    @JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn,
    @JsonProperty("scope") String scope
) {}