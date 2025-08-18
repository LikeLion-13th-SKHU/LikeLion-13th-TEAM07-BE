package com.example.ie_um.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("id_token") String idToken,
    @JsonProperty("response_type") String responseType // code
) {}