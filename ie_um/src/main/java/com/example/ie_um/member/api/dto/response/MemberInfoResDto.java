package com.example.ie_um.member.api.dto.response;

import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.domain.Gender;
import lombok.Builder;

@Builder
public record MemberInfoResDto(
        Long id,
        String email,
        String name,
        String nickName,
        Gender gender,
        int age
) {
    public static MemberInfoResDto from(Member member) {
        return MemberInfoResDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickName(member.getNickName())
                .gender(member.getGender())
                .age(member.getAge())
                .build();
    }
}
