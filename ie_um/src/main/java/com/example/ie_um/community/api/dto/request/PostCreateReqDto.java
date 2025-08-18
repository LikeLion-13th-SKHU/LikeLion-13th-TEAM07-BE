package com.example.ie_um.community.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateReqDto {
    private Long resourceId;
    private String title;
    private String content;
}
