package com.example.ie_um.resource.api.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResourceReqDto(
        Long id,
        String name,
        String description,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
