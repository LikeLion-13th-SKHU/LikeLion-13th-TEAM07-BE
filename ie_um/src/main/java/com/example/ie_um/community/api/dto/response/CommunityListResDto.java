package com.example.ie_um.community.api.dto.response;

import java.util.List;

public record CommunityListResDto(
        List<CommunityInfoResDto> communityInfoResDtos
) {
    public static CommunityListResDto from(List<CommunityInfoResDto> communityInfoResDtoList) {
        return new  CommunityListResDto(communityInfoResDtoList);
    }
}
