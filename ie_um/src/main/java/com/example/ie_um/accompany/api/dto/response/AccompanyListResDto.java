package com.example.ie_um.accompany.api.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record AccompanyListResDto(
        List<AccompanyInfoResDto> accompanyInfoResDtos
) {
    public static AccompanyListResDto from(List<AccompanyInfoResDto> accompanyInfoResDtoList) {
        return new AccompanyListResDto(accompanyInfoResDtoList);
    }
}
