package com.example.ie_um.accompany.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AccompanyReqDto(
        String title,
        String content,
        int maxPersonnel,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime time,
        String address
) {
}
