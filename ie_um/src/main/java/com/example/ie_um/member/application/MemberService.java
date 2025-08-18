package com.example.ie_um.member.application;

import com.example.ie_um.member.api.dto.request.MemberUpdateReqDto;
import com.example.ie_um.member.api.dto.response.MemberInfoResDto;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.repository.MemberRepository;
import com.example.ie_um.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResDto getMemberInfoByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        return MemberInfoResDto.from(member);
    }

    @Transactional
    public void validateAndUpdateMember(Long memberId, String email, MemberUpdateReqDto updateReqDto) {
        // 1. memberId로 사용자를 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 조회된 사용자의 이메일이 현재 로그인된 사용자의 이메일과 일치하는지 확인 (보안 강화)
        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("자신의 정보만 수정할 수 있습니다.");
        }

        // 3. DTO를 사용하여 엔티티 업데이트
        member.update(updateReqDto.name(), updateReqDto.nickName(), updateReqDto.gender());
    }
}
