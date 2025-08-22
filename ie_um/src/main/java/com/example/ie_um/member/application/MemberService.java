package com.example.ie_um.member.application;

import com.example.ie_um.member.api.dto.request.MemberUpdateReqDto;
import com.example.ie_um.member.api.dto.response.MemberInfoResDto;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.exception.DuplicateNickNameException;
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

    public MemberInfoResDto getInfo(Long currentMemberId) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

        return MemberInfoResDto.from(member);
    }

    @Transactional
    public void update(Long currentMemberId, MemberUpdateReqDto updateReqDto) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

        // 새로운 닉네임이 기존 닉네임과 다른 경우에만 중복 검사
        if (!member.getNickName().equals(updateReqDto.nickName())) {
            // 새로운 닉네임이 이미 존재하는지 확인
            if (memberRepository.existsByNickName(updateReqDto.nickName())) {
                throw new DuplicateNickNameException("이미 사용 중인 닉네임입니다.");
            }
        }

        member.update(updateReqDto.nickName(), updateReqDto.gender(), updateReqDto.age());
    }
  
    @Transactional
    public void updateProfileImg(Long currentMemberId, String profileImg) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
        member.updateImage(profileImg);
    }
}
