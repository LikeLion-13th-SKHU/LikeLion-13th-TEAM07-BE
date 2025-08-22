package com.example.ie_um.community.api.dto.request;

public record CommunityCreateReqDto(
        String title,
        String content,
        String address
) {
}
