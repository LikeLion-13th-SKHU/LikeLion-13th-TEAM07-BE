package com.example.ie_um.member.api.dto.request;

import com.example.ie_um.member.domain.Gender;
import lombok.Builder;

@Builder
public record MemberUpdateReqDto(
        String name,
        String nickName,
        Gender gender
) {
}
