package com.example.ie_um.accompany.api;

import com.example.ie_um.accompany.api.dto.request.AccompanyReqDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyApplyListResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyInfoResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyListResDto;
import com.example.ie_um.accompany.application.AccompanyService;
import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accompanies")
@Tag(name = "Accompany", description = "동행그룹 관련 API")
public class AccompanyController {
    private final AccompanyService accompanyService;

    @Operation(
            summary = "동행그룹 생성",
            description = "동행그룹을 생성합니다."
    )
    @PostMapping
    public RspTemplate<String> create(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @RequestBody AccompanyReqDto accompanyReqDto) {
        accompanyService.create(memberId, accompanyReqDto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "동행그룹이 성공적으로 생성되었습니다."
        );
    }

    @Operation(
            summary = "동행그룹 상세조회",
            description = "동행그룹 상세 정보를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccompanyInfoResDto.class)
            )
    )
    @GetMapping("/{accompanyId}")
    public RspTemplate<AccompanyInfoResDto> getDetail(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                                      @PathVariable(value = "accompanyId") Long accompanyId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 상세정보가 성공적으로 조회되었습니다.",
                accompanyService.getDetail(memberId, accompanyId)
        );
    }

    @Operation(
            summary = "동행그룹 전체조회",
            description = "동행그룹 전체 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccompanyListResDto.class)
            )
    )
    @GetMapping
    public RspTemplate<AccompanyListResDto> getAll(@Parameter(hidden = true) @AuthenticatedId Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 전체가 성공적으로 조회되었습니다.",
                accompanyService.getAll(memberId)
        );
    }

    @Operation(
            summary = "나의 동행그룹 조회",
            description = "사용자가 포함된 동행그룹을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccompanyListResDto.class)
            )
    )
    @GetMapping("/member")
    public RspTemplate<AccompanyListResDto> getByMemberId(@Parameter(hidden = true) @AuthenticatedId Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자가 포함된 동행그룹이 성공적으로 조회되었습니다.",
                accompanyService.getByMemberId(memberId)
        );
    }

    @Operation(
            summary = "신청한 동행그룹 조회",
            description = "사용자가 신청한 동행그룹을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccompanyListResDto.class)
            )
    )
    @GetMapping("/apply")
    public RspTemplate<AccompanyListResDto> getApplied(@Parameter(hidden = true) @AuthenticatedId Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자가 신청한 동행그룹이 성공적으로 조회되었습니다.",
                accompanyService.getApplied(memberId)
        );
    }

    @Operation(
            summary = "동행그룹 신청자 목록",
            description = "로그인 한 생성한 동행그룹에 신청한 사용자 목록을 조회합니다. 생성자(OWNER)만 가능합니다."
    )
    @GetMapping("/apply/member/{accompanyId}")
    public RspTemplate<AccompanyApplyListResDto> getAppliedMember(
            @Parameter(hidden = true) @AuthenticatedId Long memberId,
            @PathVariable(value = "accompanyId") Long accompanyId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "로그인 한 사용자가 생성한 동행그룹에 신청한 사용자 목록이 성공적으로 조회되었습니다.",
                accompanyService.getAppliedMember(memberId, accompanyId)
        );
    }

    @Operation(
            summary = "동행그룹 수정",
            description = "동행그룹을 수정합니다."
    )
    @PutMapping("/{accompanyId}")
    public RspTemplate<String> update(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @PathVariable(value = "accompanyId") Long accompanyId,
                                      @RequestBody AccompanyReqDto accompanyReqDto) {
        accompanyService.update(memberId, accompanyId, accompanyReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹이 성공적으로 수정되었습니다."
        );
    }

    @Operation(
            summary = "동행그룹 삭제, 탈퇴",
            description = "생성자는 동행그룹을 삭제합니다. 참가자는 동행그룹을 탈퇴합니다."
    )
    @DeleteMapping("/{accompanyId}")
    public RspTemplate<String> leave(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                     @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.leave(memberId, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹을 성공적으로 삭제/탈퇴했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청",
            description = "동행그룹에 신청합니다. 참가자와 신청자는 또 신청할 수 없습니다."
    )
    @PostMapping("/apply/{accompanyId}")
    public RspTemplate<String> apply(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                     @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.apply(memberId, accompanyId);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "동행그룹을 성공적으로 신청했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청 취소",
            description = "동행그룹 신청을 취소합니다."
    )
    @DeleteMapping("/unapply/{accompanyId}")
    public RspTemplate<String> unapply(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                       @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.unapply(memberId, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 선청을 성공적으로 취소했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청 수락",
            description = "동행그룹 신청을 수락합니다. 생성자(OWNER)만 가능합니다."
    )
    @PostMapping("/{accompanyId}/accept/{applicantId}")
    public RspTemplate<String> accept(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @PathVariable(value = "accompanyId") Long accompanyId,
                                      @PathVariable(value = "applicantId") Long applicantId) {
        log.info("controller accompanyId={}, applicantId={}", accompanyId, applicantId);
        accompanyService.accept(memberId, accompanyId, applicantId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 신청을 성공적으로 수락했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청 거절",
            description = "동행그룹 신청을 거절합니다. 생성자(OWNER)만 가능합니다."
    )
    @PostMapping("/{accompanyId}/reject/{applicantId}")
    public RspTemplate<String> reject(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                      @PathVariable(value = "accompanyId") Long accompanyId,
                                      @PathVariable(value = "applicantId") Long applicantId) {
        accompanyService.reject(memberId, accompanyId, applicantId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 신청을 성공적으로 거절했습니다."
        );
    }
}
