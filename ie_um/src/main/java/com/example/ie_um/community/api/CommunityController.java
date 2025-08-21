package com.example.ie_um.community.api;

import com.example.ie_um.community.api.dto.request.PostCreateReqDto;
import com.example.ie_um.community.api.dto.request.PostUpdateReqDto;
import com.example.ie_um.community.api.dto.response.PostResDto;
import com.example.ie_um.community.application.CommunityService;
import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Community", description = "게시글 및 좋아요 관련 API")
public class CommunityController {
    private final CommunityService communityService;

    @Operation(
            summary = "커뮤니티 글 작성",
            description = "새로운 게시글을 작성합니다."
    )
    @ApiResponses({
            @ApiResponse(description = "게시글이 성공적으로 작성되었습니다."),
            @ApiResponse(description = "유효하지 않은 요청 본문입니다."),
            @ApiResponse(description = "인증에 실패했습니다.")
    })
    @PostMapping
    public RspTemplate<Void> createPost(@RequestBody PostCreateReqDto reqDto,
                                        @Parameter(hidden = true) @AuthenticatedId Long memberId) {
        communityService.createPost(reqDto, memberId);
        return new RspTemplate<>(HttpStatus.CREATED, "게시글이 성공적으로 작성되었습니다.", null);
    }

    @Operation(summary = "커뮤니티 글 전체 조회", description = "모든 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    description = "전체 게시글을 성공적으로 조회했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            )
    })
    @GetMapping
    public RspTemplate<List<PostResDto>> getAllPosts() {
        List<PostResDto> resDtoList = communityService.getAllPosts();
        return new RspTemplate<>(HttpStatus.OK, "전체 게시글을 성공적으로 조회했습니다.", resDtoList);
    }

    @Operation(summary = "커뮤니티 글 상세 조회", description = "특정 게시글의 상세 내용을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    description = "게시글을 성공적으로 조회했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResDto.class)
                    )
            ),
            @ApiResponse(description = "게시글을 찾을 수 없습니다.")
    })
    @GetMapping("/{postId}")
    public RspTemplate<PostResDto> getPost(@Parameter(description = "조회할 게시글 ID") @PathVariable(value = "postId") Long postId) {
        PostResDto resDto = communityService.getPost(postId);
        return new RspTemplate<>(HttpStatus.OK, "게시글을 성공적으로 조회했습니다.", resDto);
    }

    @Operation(summary = "커뮤니티 글 수정", description = "특정 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    description = "게시글이 성공적으로 수정되었습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResDto.class)
                    )
            ),
            @ApiResponse(description = "인증 실패"),
            @ApiResponse(description = "게시글 수정 권한이 없습니다."),
            @ApiResponse(description = "게시글을 찾을 수 없습니다.")
    })
    @PutMapping("/{postId}")
    public RspTemplate<PostResDto> updatePost(@Parameter(description = "수정할 게시글 ID") @PathVariable(value = "postId") Long postId,
                                              @RequestBody PostUpdateReqDto reqDto,
                                              @Parameter(hidden = true) @AuthenticatedId Long memberId) {
        PostResDto resDto = communityService.updatePost(postId, reqDto, memberId);
        return new RspTemplate<>(HttpStatus.OK, "게시글이 성공적으로 수정되었습니다.", resDto);
    }

    @Operation(summary = "커뮤니티 글 삭제", description = "특정 게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(description = "게시글이 성공적으로 삭제되었습니다."),
            @ApiResponse(description = "인증 실패"),
            @ApiResponse(description = "게시글 삭제 권한이 없습니다."),
            @ApiResponse(description = "게시글을 찾을 수 없습니다.")
    })
    @DeleteMapping("/{postId}")
    public RspTemplate<Void> deletePost(@Parameter(description = "삭제할 게시글 ID") @PathVariable(value = "postId") Long postId,
                                        @Parameter(hidden = true) @AuthenticatedId Long memberId) {
        communityService.deletePost(postId, memberId);
        return new RspTemplate<>(HttpStatus.NO_CONTENT, "게시글이 성공적으로 삭제되었습니다.", null);
    }

    @Operation(summary = "커뮤니티 글 좋아요 등록", description = "특정 게시글에 좋아요를 등록합니다.")
    @ApiResponses({
            @ApiResponse(description = "좋아요가 성공적으로 등록되었습니다."),
            @ApiResponse(description = "인증 실패"),
            @ApiResponse(description = "게시글을 찾을 수 없습니다.")
    })
    @PostMapping("/likes/{postId}")
    public RspTemplate<Void> likePost(@Parameter(description = "좋아요할 게시글 ID") @PathVariable(value = "postId") Long postId,
                                      @Parameter(hidden = true) @AuthenticatedId Long memberId) {
        communityService.likePost(postId, memberId);
        return new RspTemplate<>(HttpStatus.CREATED, "게시글에 좋아요를 눌렀습니다.", null);
    }

    @Operation(summary = "커뮤니티 글 좋아요 취소", description = "특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(description = "좋아요가 성공적으로 취소되었습니다."),
            @ApiResponse(description = "인증 실패"),
            @ApiResponse(description = "좋아요를 찾을 수 없습니다.")
    })
    @DeleteMapping("/likes/{postId}")
    public RspTemplate<Void> unlikePost(@Parameter(description = "좋아요를 취소할 게시글 ID") @PathVariable(value = "postId") Long postId,
                                        @Parameter(hidden = true) @AuthenticatedId Long memberId) {
        communityService.unlikePost(postId, memberId);
        return new RspTemplate<>(HttpStatus.NO_CONTENT, "게시글 좋아요를 성공적으로 취소했습니다.", null);
    }
}
