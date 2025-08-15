package com.example.ie_um.accompany.api.dto.response;

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
        ResourceResDto place,
        String status
) {
}
