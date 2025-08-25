package com.example.ie_um.accompany.api.dto.response;

import com.example.ie_um.accompany.domain.Accompany;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record AccompanyInfoResDto(
        Long id,
        String title,
        String content,
        int maxPersonnel,
        int currentPersonnel,
        String time,
        String address,
        String role
) {
    public static AccompanyInfoResDto from(Accompany accompany, String role) {
        return new AccompanyInfoResDto(
                accompany.getId(),
                accompany.getTitle(),
                accompany.getContent(),
                accompany.getMaxPersonnel(),
                accompany.getCurrentPersonnel(),
                accompany.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                accompany.getAddress(),
                role
        );
    }
}
