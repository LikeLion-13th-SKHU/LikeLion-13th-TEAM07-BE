package com.example.ie_um.accompany.api.dto.response;

import lombok.Builder;

@Builder
public record AccompanyApplyResDto(
        Long id,
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String place,
        String status
) {
}
