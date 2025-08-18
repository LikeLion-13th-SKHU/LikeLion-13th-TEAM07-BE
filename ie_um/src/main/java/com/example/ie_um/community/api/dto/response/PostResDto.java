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
    private Long memberId; // 변경된 부분
    private String memberNickname; // 변경된 부분
    private int likesCount;
    private LocalDateTime createDate;

    @Builder
    public PostResDto(Long postId, Long resourceId, String title, String content, Long memberId, String memberNickname, int likesCount, LocalDateTime createDate) { // 변경된 부분
        this.postId = postId;
        this.resourceId = resourceId;
        this.title = title;
        this.content = content;
        this.memberId = memberId; // 변경된 부분
        this.memberNickname = memberNickname; // 변경된 부분
        this.likesCount = likesCount;
        this.createDate = createDate;
    }
}
