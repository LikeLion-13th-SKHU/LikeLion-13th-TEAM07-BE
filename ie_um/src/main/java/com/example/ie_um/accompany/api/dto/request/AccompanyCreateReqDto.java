package com.example.ie_um.accompany.api.dto.request;

import java.math.BigDecimal;

public record AccompanyCreateReqDto(
        // Accompany
        String title,
        String content,
        int maxPersonnel,
        String time,
        // Resource
        String name,
        String description,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
