package com.example.ie_um.community.api;

import com.example.ie_um.community.api.dto.request.CommunityCreateReqDto;
import com.example.ie_um.community.api.dto.request.CommunityUpdateReqDto;
import com.example.ie_um.community.api.dto.response.CommunityInfoResDto;
import com.example.ie_um.community.api.dto.response.CommunityListResDto;
import com.example.ie_um.community.application.CommunityService;
import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
@Tag(name = "Community", description = "커뮤니티 관련 API")
public class CommunityController {
    private final CommunityService communityService;

    @Operation(
            summary = "커뮤니티 생성",
            description = "커뮤니티를 생성합니다."
    )
    @PostMapping
    public RspTemplate<String> create(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @RequestBody CommunityCreateReqDto dto) {
        communityService.create(memberId, dto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "커뮤니티가 성공적으로 생성되었습니다."
        );
    }

    @Operation(
            summary = "커뮤니티 상세조회",
            description = "커뮤니티를 상세 정보를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation =  CommunityInfoResDto.class)
            )
    )
    @GetMapping("/{communityId}")
    public RspTemplate<CommunityInfoResDto> getDetail(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                                      @PathVariable(value = "communityId") Long communityId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티 상세조회가 성공적으로 조회되었습니다.",
                communityService.getDetail(memberId, communityId)
        );
    }

    @Operation(
            summary = "커뮤니티 전체조회",
            description = "커뮤니티 전체 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation =  CommunityListResDto.class)
            )
    )
    @GetMapping
    public RspTemplate<CommunityListResDto> getAll(@Parameter(hidden = true) @AuthenticatedId Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티 전체가 성공적으로 조회되었습니다.",
                communityService.getAll()
        );
    }

    @Operation(
            summary = "내가 쓴 커뮤니티 조회",
            description = "로그인 한 사용자가 작성한 커뮤니티를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation =  CommunityListResDto.class)
            )
    )
    @GetMapping("/member")
    public RspTemplate<CommunityListResDto> getByMemberId(@Parameter(hidden = true) @AuthenticatedId Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자가 작성한 커뮤니티가 성공적으로 조회되었습니다.",
                communityService.getByMemberId(memberId)
        );
    }

    @Operation(
            summary = "커뮤니티 수정",
            description = "커뮤니티를 수정합니다. 생성자만 수정할 수 있습니다."
    )
    @PutMapping("/{communityId}")
    public RspTemplate<String> update(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                    @PathVariable(value = "communityId") Long communityId,
                                    @RequestBody CommunityUpdateReqDto dto) {
        communityService.update(memberId, communityId, dto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티가 성공적으로 수정되었습니다."
        );
    }

    @Operation(
            summary = "커뮤니티 삭제",
            description = "커뮤니티를 삭제합니다. 생성자만 삭제할 수 있습니다."
    )
    @DeleteMapping("/{communityId}")
    public RspTemplate<String> delete(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @PathVariable(value = "communityId") Long communityId) {
        communityService.delete(memberId, communityId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티가 성공적으로 삭제되었습니다."
        );
    }

    @PostMapping("/likes/{communityId}")
    public RspTemplate<String> saveLike(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                        @PathVariable(value = "communityId") Long communityId) {
        communityService.saveLike(memberId, communityId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티 좋아요가 성공적으로 등록되었습니다."
        );
    }

    @DeleteMapping("/likes/{communityId}")
    public RspTemplate<String> deleteLike(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                          @PathVariable(value = "communityId") Long communityId) {
        communityService.deleteLike(memberId, communityId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "커뮤니티 좋아요가 성공적으로 취소되었습니다."
        );
    }
}
