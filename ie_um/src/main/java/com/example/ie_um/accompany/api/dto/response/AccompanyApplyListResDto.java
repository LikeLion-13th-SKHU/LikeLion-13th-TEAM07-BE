package com.example.ie_um.accompany.api.dto.response;

import java.util.List;

public record AccompanyApplyListResDto(
        Long accompanyId,
        String role,
        List<AccompanyApplyResDto> accompanyApplyResDtos
) {
    public static AccompanyApplyListResDto of(Long accompanyId, String role, List<AccompanyApplyResDto> accompanyApplyResDto) {
        return new AccompanyApplyListResDto(
                accompanyId,
                role,
                accompanyApplyResDto);
    }
}
