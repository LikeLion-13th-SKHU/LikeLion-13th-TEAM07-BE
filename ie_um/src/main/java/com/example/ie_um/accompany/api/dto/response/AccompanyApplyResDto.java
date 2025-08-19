package com.example.ie_um.accompany.api.dto.response;

import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.resource.api.dto.response.ResourceResDto;
import lombok.Builder;

@Builder
public record AccompanyApplyResDto(
        Long id,
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String address,
        String role
) {
    public static AccompanyApplyResDto from(AccompanyMember accompanyMember) {
        Accompany accompany = accompanyMember.getAccompany();
        return new AccompanyApplyResDto(
                accompany.getId(),
                accompany.getTitle(),
                accompany.getContent(),
                accompany.getMaxPersonnel(),
                accompany.getCurrentPersonnel(),
                accompany.getTime(),
                accompany.getAddress(),
                accompanyMember.getRole().getDescription()
        );
    }
}
