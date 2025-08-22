package com.example.ie_um.community.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResDto {
    private List<PostResDto> posts;
}
