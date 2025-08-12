package com.example.ie_um.accompany.api;

import com.example.ie_um.accompany.api.dto.request.AccompanyReqDto;
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
    public RspTemplate<String> create(@PathVariable(value = "memberId") Long memberId, AccompanyReqDto accompanyReqDto) {
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
                                      AccompanyReqDto accompanyReqDto) {
        accompanyService.update(memberId, accompanyId, accompanyReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹이 성공적으로 수정되었습니다."
        );
    }

    @Operation(
            summary = "동행그룹 삭제",
            description = "동행그룹을 삭제합니다."
    )
    @DeleteMapping("/{memberId}/{accompanyId}")
    public RspTemplate<String> delete(@PathVariable(value = "memberId") Long memberId,
                                      @PathVariable(value = "accompanyId") Long accompanyId) {
        accompanyService.delete(memberId, accompanyId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "동행그룹이 성공적으로 삭제되었습니다."
        );
    }
}
