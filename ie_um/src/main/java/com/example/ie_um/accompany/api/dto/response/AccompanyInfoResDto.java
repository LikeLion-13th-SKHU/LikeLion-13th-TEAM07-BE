package com.example.ie_um.accompany.api.dto.response;

import lombok.Builder;

@Builder
public record AccompanyInfoResDto(
        Long id,
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String place
) {
}
