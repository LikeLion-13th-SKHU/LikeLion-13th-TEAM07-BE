package com.example.ie_um.community.api.dto.request;

public record CommunityUpdateReqDto(
        String title,
        String content,
        String address
) {
}
