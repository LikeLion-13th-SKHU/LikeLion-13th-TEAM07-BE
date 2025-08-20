package com.example.ie_um.member.api;

import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.template.RspTemplate;
import com.example.ie_um.member.api.dto.request.MemberUpdateReqDto;
import com.example.ie_um.member.api.dto.response.MemberInfoResDto;
import com.example.ie_um.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "나의 프로필 정보 조회",
            description = "로그인된 사용자의 프로필 정보를 조회합니다."
    )
    @GetMapping
    public RspTemplate<MemberInfoResDto> getInfo(@Parameter(hidden = true) @AuthenticatedId Long currentMemberId) {
        MemberInfoResDto memberInfo = memberService.getInfo(currentMemberId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자 프로필 정보가 성공적으로 조회되었습니다.",
                memberInfo
        );
    }

    @Operation(
            summary = "개인정보 수정",
            description = "로그인된 사용자의 프로필 정보를 수정합니다."
    )
    @PutMapping
    public RspTemplate<String> update(
            @Parameter(hidden = true) @AuthenticatedId Long currentMemberId,
            @RequestBody MemberUpdateReqDto updateReqDto) {

        memberService.update(currentMemberId, updateReqDto);

        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자 프로필 정보가 성공적으로 수정되었습니다."
        );
    }
}
