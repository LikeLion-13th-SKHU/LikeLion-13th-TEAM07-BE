package com.example.ie_um.resource.api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResourceResDto(
        Long id,
        String name,
        String description,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
