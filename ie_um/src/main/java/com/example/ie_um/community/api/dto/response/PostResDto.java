package com.example.ie_um.community.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResDto {
    private Long postId;
    private Long resourceId;
    private String title;
    private String content;
    private Long memberId;
    private String memberNickname;
    private int likesCount;
    private LocalDateTime createDate;

    @Builder
    public PostResDto(Long postId, Long resourceId, String title, String content, Long memberId, String memberNickname, int likesCount, LocalDateTime createDate) {
        this.postId = postId;
        this.resourceId = resourceId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.likesCount = likesCount;
        this.createDate = createDate;
    }
}
