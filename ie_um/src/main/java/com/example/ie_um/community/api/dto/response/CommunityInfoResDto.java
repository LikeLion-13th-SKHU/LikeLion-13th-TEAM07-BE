package com.example.ie_um.community.api.dto.response;

import com.example.ie_um.community.domain.Community;
import lombok.Builder;

@Builder
public record CommunityInfoResDto(
        String title,
        String content,
        String address
) {
    public static CommunityInfoResDto from(Community community) {
        return new CommunityInfoResDto(
                community.getTitle(),
                community.getContent(),
                community.getAddress()
        );
    }
}
