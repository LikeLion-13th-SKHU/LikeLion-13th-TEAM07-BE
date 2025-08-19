package com.example.ie_um.accompany.api.dto.response;

import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.resource.api.dto.response.ResourceResDto;
import lombok.Builder;

@Builder
public record AccompanyInfoResDto(
        Long id,
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String address
) {
    public static AccompanyInfoResDto from(Accompany accompany) {
        return new AccompanyInfoResDto(
                accompany.getId(),
                accompany.getTitle(),
                accompany.getContent(),
                accompany.getMaxPersonnel(),
                accompany.getCurrentPersonnel(),
                accompany.getTime(),
                accompany.getAddress()
        );
    }
}
