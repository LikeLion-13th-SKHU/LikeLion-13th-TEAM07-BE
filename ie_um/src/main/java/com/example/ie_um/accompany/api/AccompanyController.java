package com.example.ie_um.accompany.api;

import com.example.ie_um.accompany.api.dto.request.AccompanyCreateReqDto;
import com.example.ie_um.accompany.api.dto.request.AccompanyUpdateReqDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyInfoResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyListResDto;
import com.example.ie_um.accompany.application.AccompanyService;
import com.example.ie_um.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{memberId}")
    public RspTemplate<String> create(@PathVariable(value = "memberId") Long memberId, @RequestBody AccompanyCreateReqDto accompanyCreateReqDto) {
         accompanyService.create(memberId, accompanyCreateReqDto);
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
    public RspTemplate<AccompanyInfoResDto> getDetail(@PathVariable(value = "accompanyId") Long accompanyId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 상세정보가 성공적으로 조회되었습니다.",
                accompanyService.getDetail(accompanyId)
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
    public RspTemplate<AccompanyListResDto> getAll() {
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 전체가 성공적으로 조회되었습니다.",
                accompanyService.getAll()
        );
    }

    @Operation(
            summary = "동행그룹 수정",
            description = "동행그룹을 수정합니다."
    )
    @PutMapping("/{memberId}/{accompanyId}")
    public RspTemplate<String> update(@PathVariable(value = "memberId") Long memberId,
                                      @PathVariable(value = "accompanyId") Long accompanyId,
                                      @RequestBody AccompanyUpdateReqDto accompanyUpdateReqDto) {
        accompanyService.update(memberId, accompanyId, accompanyUpdateReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹이 성공적으로 수정되었습니다."
        );
    }

    @Operation(
            summary = "동행그룹 삭제, 탈퇴",
            description = "생성자는 동행그룹을 삭제합니다. 참가자는 동행그룹을 탈퇴합니다."
    )
    @DeleteMapping("/{memberId}/{accompanyId}")
    public RspTemplate<String> leave(@PathVariable(value = "memberId") Long memberId,
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
    @PostMapping("/apply/{memberId}/{accompanyId}")
    public RspTemplate<String> apply(@PathVariable(value = "memberId") Long memberId,
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
    @DeleteMapping("/unapply/{memberId}/{accompanyId}")
    public RspTemplate<String> unapply(@PathVariable(value = "memberId") Long memberId,
                                       @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.unapply(memberId, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 선청을 성공적으로 취소했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청 수락",
            description = "동행그룹 신청을 수락합니다. 생성자만 거절할 수 있습니다."
    )
    @PostMapping("/accept/{ownerId}/{memberIdToAccept}/{accompanyId}")
    public RspTemplate<String> accept(@PathVariable(value = "ownerId") Long ownerId,
                                      @PathVariable(value = "memberIdToAccept") Long memberIdToAccept,
                                      @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.accept(ownerId, memberIdToAccept, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 선청을 성공적으로 수락했습니다."
        );
    }

    @Operation(
            summary = "동행그룹 신청 거절",
            description = "동행그룹 신청을 거절합니다. 생성자만 거절할 수 있습니다."
    )
    @PostMapping("/reject/{ownerId}/{memberIdToReject}/{accompanyId}")
    public RspTemplate<String> reject(@PathVariable(value = "ownerId") Long ownerId,
                                      @PathVariable(value = "memberIdToReject") Long memberIdToReject,
                                      @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.reject(ownerId, memberIdToReject, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹 선청을 성공적으로 거절했습니다."
        );
    }

}
