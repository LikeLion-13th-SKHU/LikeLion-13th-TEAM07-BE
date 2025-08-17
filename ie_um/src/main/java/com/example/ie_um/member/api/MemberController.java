package com.example.ie_um.member.api;

import com.example.ie_um.global.template.RspTemplate;
import com.example.ie_um.member.api.dto.response.MemberInfoResDto;
import com.example.ie_um.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/me")
    public RspTemplate<MemberInfoResDto> getMyProfile(@AuthenticationPrincipal String email) {
        // @AuthenticationPrincipal에서 이메일을 가져와 Service 계층으로 전달
        MemberInfoResDto memberInfo = memberService.getMemberInfoByEmail(email);
        return new RspTemplate<>(
                HttpStatus.OK,
                "사용자 프로필 정보가 성공적으로 조회되었습니다.",
                memberInfo
        );

    }
    }
