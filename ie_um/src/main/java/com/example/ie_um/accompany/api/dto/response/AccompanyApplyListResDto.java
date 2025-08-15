package com.example.ie_um.accompany.api.dto.response;

import java.util.List;

public record AccompanyApplyListResDto(
        List<AccompanyApplyResDto> accompanyApplyResDtos
) {
    public static AccompanyApplyListResDto from(List<AccompanyApplyResDto> accompanyApplyResDto) {
        return new AccompanyApplyListResDto(accompanyApplyResDto);
    }
}
