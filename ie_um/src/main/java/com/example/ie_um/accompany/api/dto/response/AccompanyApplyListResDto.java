package com.example.ie_um.accompany.api.dto.response;

import java.util.List;

public record AccompanyApplyListResDto(
        Long accompanyId,
        List<AccompanyApplyResDto> applicants
) {
    // 인자를 2개만 받는 of 메서드
    public static AccompanyApplyListResDto of(Long accompanyId, List<AccompanyApplyResDto> applicants) {
        return new AccompanyApplyListResDto(accompanyId, applicants);
    }
}
