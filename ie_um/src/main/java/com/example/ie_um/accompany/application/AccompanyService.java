package com.example.ie_um.accompany.application;

import com.example.ie_um.accompany.api.dto.request.AccompanyCreateReqDto;
import com.example.ie_um.accompany.api.dto.request.AccompanyUpdateReqDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyInfoResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyListResDto;
import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.accompany.domain.repository.AccompanyMemberRepository;
import com.example.ie_um.accompany.domain.repository.AccompanyRepository;
import com.example.ie_um.accompany.exception.AccompanyInvalidGroupException;
import com.example.ie_um.accompany.exception.AccompanyNotFoundException;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.domain.repository.MemberRepository;
import com.example.ie_um.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccompanyService {
    private final MemberRepository memberRepository;
    private final AccompanyRepository accompanyRepository;
    private final AccompanyMemberRepository accompanyMemberRepository;

    @Transactional
    public void create(Long memberId, AccompanyCreateReqDto accompanyCreateReqDto) {
        // TODO: 로그인 한 사용자 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
        Accompany accompany = Accompany.builder()
                .title(accompanyCreateReqDto.title())
                .content(accompanyCreateReqDto.content())
                .maxPersonnel(accompanyCreateReqDto.maxPersonnel())
                .currentPersonnel(1)
                .time(accompanyCreateReqDto.time())
                .place(accompanyCreateReqDto.place())
                .build();

        AccompanyMember accompanyMember = AccompanyMember.builder()
                .member(member)
                .accompany(accompany)
                .isOwner(true)
                .build();

        accompanyRepository.save(accompany);
        accompanyMemberRepository.save(accompanyMember);
    }

    public AccompanyInfoResDto getDetail(Long accompanyId) {
        Accompany accompany = accompanyRepository.findById(accompanyId)
                .orElseThrow(() -> new AccompanyNotFoundException("동행 그룹을 찾을 수 없습니다."));

        return AccompanyInfoResDto.builder()
                .title(accompany.getTitle())
                .content(accompany.getContent())
                .maxPersonnel(accompany.getMaxPersonnel())
                .currentPersonnel(accompany.getCurrentPersonnel())
                .time(accompany.getTime())
                .place(accompany.getPlace())
                .build();
    }

    public AccompanyListResDto getAll() {
        List<Accompany> accompanyList = accompanyRepository.findAll();
        List<AccompanyInfoResDto> accompanyListResDtoList = accompanyList.stream()
                .map(accompany -> AccompanyInfoResDto.builder()
                        .title(accompany.getTitle())
                        .content(accompany.getContent())
                        .maxPersonnel(accompany.getMaxPersonnel())
                        .currentPersonnel(accompany.getCurrentPersonnel())
                        .time(accompany.getTime())
                        .place(accompany.getPlace())
                        .build())
                .toList();
        return AccompanyListResDto.from(accompanyListResDtoList);
    }

    @Transactional
    public void update(Long memberId, Long accompanyId, AccompanyUpdateReqDto accompanyUpdateReqDto) {
        Accompany accompany = validateOwnerAndGetAccompany(memberId, accompanyId);

        accompany.update(accompanyUpdateReqDto.title(),
                accompanyUpdateReqDto.content(),
                accompanyUpdateReqDto.maxPersonnel(),
                accompanyUpdateReqDto.currentPersonnel(),
                accompanyUpdateReqDto.time(),
                accompanyUpdateReqDto.place());
    }

    @Transactional
    public void delete(Long memberId, Long accompanyId) {
        Accompany accompany = validateOwnerAndGetAccompany(memberId, accompanyId);

        accompanyMemberRepository.deleteByAccompanyId(accompanyId);
        accompanyRepository.delete(accompany);
    }

    private Accompany validateOwnerAndGetAccompany(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("해당 동행 그룹에 참여하지 않았습니다."));

        if (!accompanyMember.isOwner()) {
            throw new AccompanyInvalidGroupException("동행그룹 생성자만 수정/삭제할 수 있습니다.");
        }

        return accompanyMember.getAccompany();
    }
}
