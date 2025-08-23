package com.example.ie_um.community.api.dto.response;

import com.example.ie_um.community.domain.Community;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record CommunityInfoResDto(
        Long id,
        String title,
        String content,
        String address,
        boolean isLike,
        Integer likeCount,
        Long memberId,
        String nickName,
        String profileUrl,
        String createDate
) {
    public static CommunityInfoResDto from(Community community, boolean isLike) {
        return new CommunityInfoResDto(
                community.getId(),
                community.getTitle(),
                community.getContent(),
                community.getAddress(),
                isLike,
                community.getLikeCount(),
                community.getMember().getId(),
                community.getMember().getNickName(),
                community.getMember().getProfileImg(),
                community.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
    }
}
