package com.example.ie_um.accompany.api.dto.request;

public record AccompanyCreateReqDto(
        String title,
        String content,
        int maxPersonnel,
        String time,
        String place
) {
}
