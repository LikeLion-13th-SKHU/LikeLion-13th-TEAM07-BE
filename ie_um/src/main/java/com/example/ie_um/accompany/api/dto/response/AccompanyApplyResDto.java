package com.example.ie_um.accompany.api.dto.response;

import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.member.domain.Gender;
import lombok.Builder;

@Builder
public record AccompanyApplyResDto(
        Long memberId,
        String nickName,
        Integer age,
        Gender gender,
        String role
) {
    public static AccompanyApplyResDto from(AccompanyMember accompanyMember) {
        Accompany accompany = accompanyMember.getAccompany();
        return new AccompanyApplyResDto(
                accompanyMember.getMember().getId(),
                accompanyMember.getMember().getNickName(),
                accompanyMember.getMember().getAge(),
                accompanyMember.getMember().getGender(),
                accompanyMember.getRole().name()
        );
    }
}
