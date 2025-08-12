package com.example.ie_um.accompany.api.dto.response;

import lombok.Builder;

@Builder
public record AccompanyInfoResDto(
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        boolean isOwner,
        String time,
        String place
) {
}
