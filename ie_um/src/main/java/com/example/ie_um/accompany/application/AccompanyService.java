package com.example.ie_um.accompany.application;

import com.example.ie_um.accompany.api.dto.request.AccompanyCreateReqDto;
import com.example.ie_um.accompany.api.dto.request.AccompanyUpdateReqDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyApplyListResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyApplyResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyInfoResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyListResDto;
import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.accompany.domain.AccompanyStatus;
import com.example.ie_um.accompany.domain.repository.AccompanyMemberRepository;
import com.example.ie_um.accompany.domain.repository.AccompanyRepository;
import com.example.ie_um.accompany.exception.AccompanyInvalidGroupException;
import com.example.ie_um.accompany.exception.AccompanyNotFoundException;
import com.example.ie_um.accompany.exception.AccompanyPersonnelInvalidGroupException;
import com.example.ie_um.domain.member.entity.Member;
import com.example.ie_um.domain.member.exception.MemberNotFoundException;
import com.example.ie_um.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
                .accompanyStatus(AccompanyStatus.ACCEPTED)
                .build();

        accompanyRepository.save(accompany);
        accompanyMemberRepository.save(accompanyMember);
    }

    public AccompanyInfoResDto getDetail(Long accompanyId) {
        Accompany accompany = accompanyRepository.findById(accompanyId)
                .orElseThrow(() -> new AccompanyNotFoundException("동행 그룹을 찾을 수 없습니다."));

        return AccompanyInfoResDto.builder()
                .id(accompany.getId())
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
        return getAccompanyListResDto(accompanyList);
    }

    public AccompanyListResDto getByMemberId(Long memberId) {
        List<AccompanyMember> accompanyMembers = accompanyMemberRepository.findByMemberIdAndAccompanyStatus(memberId, AccompanyStatus.ACCEPTED);

        List<Accompany> accompanyList = accompanyMembers.stream()
                .map(AccompanyMember::getAccompany)
                .toList();

        return getAccompanyListResDto(accompanyList);
    }

    public AccompanyApplyListResDto getApplied(Long memberId) {
        List<AccompanyStatus> statuses = Arrays.asList(AccompanyStatus.PENDING, AccompanyStatus.REJECTED);

        List<AccompanyMember> appliedMembers = accompanyMemberRepository.findByMemberIdAndAccompanyStatusIn(memberId, statuses);

        List<AccompanyApplyResDto> appliedGroupDtos = appliedMembers.stream()
                .map(accompanyMember -> {
                    Accompany accompany = accompanyMember.getAccompany();
                    return AccompanyApplyResDto.builder()
                            .id(accompany.getId())
                            .title(accompany.getTitle())
                            .content(accompany.getContent())
                            .maxPersonnel(accompany.getMaxPersonnel())
                            .currentPersonnel(accompany.getCurrentPersonnel())
                            .time(accompany.getTime())
                            .place(accompany.getPlace())
                            .status(accompanyMember.getAccompanyStatus().getDescription())
                            .build();
                })
                .toList();

        return AccompanyApplyListResDto.from(appliedGroupDtos);
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
    public void leave(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("해당 동행 그룹의 참가자가 아닙니다."));

        if (accompanyMember.getAccompanyStatus() != AccompanyStatus.ACCEPTED) {
            throw new AccompanyInvalidGroupException("이미 그룹에 속해있지 않습니다.");
        }

        if (accompanyMember.isOwner()) {
            accompanyMemberRepository.deleteByAccompanyId(accompanyId);
            accompanyRepository.deleteById(accompanyId);

        } else {
            Accompany accompany = accompanyMember.getAccompany();
            accompanyMemberRepository.delete(accompanyMember);
            accompany.decreaseCurrentPersonnel();
        }
    }

    @Transactional
    public void apply(Long memberId, Long accompanyId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
        Accompany accompany = accompanyRepository.findById(accompanyId)
                .orElseThrow(() -> new AccompanyNotFoundException("동행 그룹을 찾을 수 없습니다."));

        if (accompany.getMaxPersonnel() <= accompany.getCurrentPersonnel()) {
            throw new AccompanyPersonnelInvalidGroupException("동행 그룹 정원을 초과하였습니다.");
        }

        accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .ifPresent(accompanyMember -> {
                    throw new AccompanyInvalidGroupException("이미 동행 그룹에 속해있거나 신청 대기 중입니다.");
                });

        AccompanyMember accompanyMember = AccompanyMember.builder()
                .member(member)
                .accompany(accompany)
                .isOwner(false)
                .accompanyStatus(AccompanyStatus.PENDING)
                .build();

        accompanyMemberRepository.save(accompanyMember);
    }

    @Transactional
    public void unapply(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("신청 내역이 존재하지 않습니다."));

        if (accompanyMember.getAccompanyStatus() != AccompanyStatus.PENDING) {
            throw new AccompanyInvalidGroupException("신청 대기 중인 상태에서만 취소가 가능합니다.");
        }

        accompanyMemberRepository.delete(accompanyMember);
    }

    @Transactional
    public void accept(Long ownerId, Long memberIdToAccept, Long accompanyId) {
        validateOwner(ownerId, accompanyId);

        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberIdToAccept, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("신청 내역이 존재하지 않습니다."));

        if (accompanyMember.getAccompanyStatus() != AccompanyStatus.PENDING) {
            throw new AccompanyInvalidGroupException("이미 처리된 신청입니다.");
        }

        Accompany accompany = accompanyMember.getAccompany();
        if (accompany.getMaxPersonnel() <= accompany.getCurrentPersonnel()) {
            throw new AccompanyPersonnelInvalidGroupException("동행 그룹 정원을 초과하여 수락할 수 없습니다.");
        }

        accompanyMember.updateAccompanyStatus(AccompanyStatus.ACCEPTED);
        accompany.increaseCurrentPersonnel();
    }

    @Transactional
    public void reject(Long ownerId, Long memberIdToReject, Long accompanyId) {
        validateOwner(ownerId, accompanyId);

        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberIdToReject, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("신청 내역이 존재하지 않습니다."));

        if (accompanyMember.getAccompanyStatus() != AccompanyStatus.PENDING) {
            throw new AccompanyInvalidGroupException("이미 처리된 신청입니다.");
        }

        accompanyMemberRepository.delete(accompanyMember);
    }

    private void validateOwner(Long memberId, Long accompanyId) {
        validateOwnerAndGetAccompany(memberId, accompanyId);
    }


    private Accompany validateOwnerAndGetAccompany(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("해당 동행 그룹에 참여하지 않았습니다."));

        if (!accompanyMember.isOwner()) {
            throw new AccompanyInvalidGroupException("동행그룹 생성자만 수정/삭제할 수 있습니다.");
        }

        return accompanyMember.getAccompany();
    }

    private AccompanyListResDto getAccompanyListResDto(List<Accompany> appliedGroups) {
        List<AccompanyInfoResDto> appliedGroupDtos = appliedGroups.stream()
                .map(accompany -> AccompanyInfoResDto.builder()
                        .id(accompany.getId())
                        .title(accompany.getTitle())
                        .content(accompany.getContent())
                        .maxPersonnel(accompany.getMaxPersonnel())
                        .currentPersonnel(accompany.getCurrentPersonnel())
                        .time(accompany.getTime())
                        .place(accompany.getPlace())
                        .build())
                .toList();

        return AccompanyListResDto.from(appliedGroupDtos);
    }
}