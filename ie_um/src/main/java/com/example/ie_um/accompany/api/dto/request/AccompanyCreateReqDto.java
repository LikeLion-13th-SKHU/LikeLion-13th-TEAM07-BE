package com.example.ie_um.accompany.api.dto.request;

public record AccompanyCreateReqDto(
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String place
) {
}
