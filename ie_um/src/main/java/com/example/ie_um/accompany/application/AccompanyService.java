package com.example.ie_um.accompany.application;

import com.example.ie_um.accompany.api.dto.request.AccompanyReqDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyApplyListResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyApplyResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyInfoResDto;
import com.example.ie_um.accompany.api.dto.response.AccompanyListResDto;
import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.accompany.domain.AccompanyRole;
import com.example.ie_um.accompany.domain.repository.AccompanyMemberRepository;
import com.example.ie_um.accompany.domain.repository.AccompanyRepository;
import com.example.ie_um.accompany.exception.AccompanyInvalidGroupException;
import com.example.ie_um.accompany.exception.AccompanyNotFoundException;
import com.example.ie_um.accompany.exception.AccompanyPersonnelInvalidGroupException;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.domain.repository.MemberRepository;
import com.example.ie_um.member.exception.MemberNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccompanyService {
    private final MemberRepository memberRepository;
    private final AccompanyRepository accompanyRepository;
    private final AccompanyMemberRepository accompanyMemberRepository;

    @Transactional
    public void create(Long memberId, AccompanyReqDto accompanyReqDto) {
        Member member = findMemberById(memberId);

        Accompany accompany = Accompany.builder()
                .title(accompanyReqDto.title())
                .content(accompanyReqDto.content())
                .maxPersonnel(accompanyReqDto.maxPersonnel())
                .currentPersonnel(1)
                .time(accompanyReqDto.time())
                .address(accompanyReqDto.address())
                .build();

        AccompanyMember accompanyMember = AccompanyMember.builder()
                .member(member)
                .accompany(accompany)
                .role(AccompanyRole.OWNER)
                .build();

        accompanyRepository.save(accompany);
        accompanyMemberRepository.save(accompanyMember);
    }

    public AccompanyInfoResDto getDetail(Long memberId, Long accompanyId) {
        Accompany accompany = findAccompanyById(accompanyId);

        String userRole = accompanyMemberRepository.findByMemberIdAndAccompanyId(memberId, accompanyId)
                .map(accompanyMember -> accompanyMember.getRole().name()).orElse(null);

        return AccompanyInfoResDto.from(accompany, userRole);
    }

    public AccompanyListResDto getAll(Long memberId) {
        List<Accompany> accompanyList = accompanyRepository.findAll();
        return getAccompanyListResDto(accompanyList, memberId);
    }

    public AccompanyListResDto getByMemberId(Long memberId) {
        List<AccompanyRole> roles = Arrays.asList(AccompanyRole.OWNER, AccompanyRole.ACCEPTED);
        List<AccompanyMember> accompanyMembers = accompanyMemberRepository.findByMemberIdAndRoleIn(memberId, roles);
        List<Accompany> accompanyList = accompanyMembers.stream()
                .map(AccompanyMember::getAccompany)
                .toList();
        return getAccompanyListResDto(accompanyList, memberId);
    }

    public AccompanyListResDto getApplied(Long memberId) {
        List<AccompanyRole> roles = Arrays.asList(AccompanyRole.PENDING, AccompanyRole.REJECTED);
        List<AccompanyMember> accompanyMembers = accompanyMemberRepository.findByMemberIdAndRoleIn(memberId, roles);
        List<Accompany> accompanyList = accompanyMembers.stream()
                .map(AccompanyMember::getAccompany)
                .toList();
        return getAccompanyListResDto(accompanyList, memberId);
    }

    public AccompanyApplyListResDto getAppliedMember(Long ownerId, Long accompanyId) {
        validateOwner(ownerId, accompanyId);
        List<AccompanyMember> accompanyMembers = accompanyMemberRepository
                .findByAccompanyIdAndRoleWithMember(accompanyId, AccompanyRole.PENDING);

        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompanyId(ownerId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("참여자가 아닙니다."));

        List<AccompanyApplyResDto> applicantInfos = accompanyMembers.stream()
                .map(AccompanyApplyResDto::from)
                .toList();

        return AccompanyApplyListResDto.of(accompanyId, accompanyMember.getRole().toString(), applicantInfos);
    }

    @Transactional
    public void update(Long memberId, Long accompanyId, AccompanyReqDto accompanyReqDto) {
        validateOwner(memberId, accompanyId);
        Accompany accompany = findAccompanyById(accompanyId);

        accompany.update(accompanyReqDto.title(),
                accompanyReqDto.content(),
                accompanyReqDto.maxPersonnel(),
                accompanyReqDto.time(),
                accompanyReqDto.address());
    }

    @Transactional
    public void leave(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository
                .findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("참여자가 아닙니다."));

        Accompany accompany = accompanyMember.getAccompany();

        if (accompanyMember.getRole() != AccompanyRole.ACCEPTED && accompanyMember.getRole() != AccompanyRole.OWNER) {
            throw new AccompanyInvalidGroupException("참여가 수락된 상태에서만 탈퇴가 가능합니다.");
        }

        if (accompanyMember.getRole() == AccompanyRole.OWNER) {
            accompanyMemberRepository.deleteByAccompanyId(accompanyId);
            accompanyRepository.deleteById(accompanyId);
        } else {
            accompanyMember.updateAccompanyStatus(AccompanyRole.LEAVE);
            accompany.decreaseCurrentPersonnel();
        }
    }

    @Transactional
    public void apply(Long memberId, Long accompanyId) {
        Member member = findMemberById(memberId);
        Accompany accompany = findAccompanyById(accompanyId);

        if (accompany.getMaxPersonnel() <= accompany.getCurrentPersonnel()) {
            throw new AccompanyPersonnelInvalidGroupException("정원이 초과되었습니다.");
        }

        if (accompanyMemberRepository.existsByMemberIdAndAccompany(memberId, accompany)) {
            throw new AccompanyInvalidGroupException("이미 신청/참여 중인 사용자입니다.");
        }

        AccompanyMember applyMember = AccompanyMember.builder()
                .member(member)
                .accompany(accompany)
                .role(AccompanyRole.PENDING)
                .build();

        accompanyMemberRepository.save(applyMember);
    }

    @Transactional
    public void unapply(Long memberId, Long accompanyId) {
        Accompany accompany = findAccompanyById(accompanyId);
        AccompanyMember accompanyMember = accompanyMemberRepository.findByMemberIdAndAccompany(memberId, accompany)
                .orElseThrow(() -> new AccompanyInvalidGroupException("신청 내역이 존재하지 않습니다."));

        if (accompanyMember.getRole() != AccompanyRole.PENDING) {
            throw new AccompanyInvalidGroupException("신청 대기 중인 상태에서만 취소가 가능합니다.");
        }

        accompanyMemberRepository.delete(accompanyMember);
    }

    @Transactional
    public void accept(Long ownerId, Long accompanyId, Long applicantId) {
        AccompanyMember accompanyMember = findAndValidatePendingApplicant(ownerId, accompanyId, applicantId);

        Accompany accompany = accompanyMember.getAccompany();
        if (accompany.getMaxPersonnel() <= accompany.getCurrentPersonnel()) {
            throw new AccompanyPersonnelInvalidGroupException("정원이 초과되었습니다.");
        }

        accompanyMember.updateAccompanyStatus(AccompanyRole.ACCEPTED);
        accompany.increaseCurrentPersonnel();
    }

    @Transactional
    public void reject(Long ownerId, Long accompanyId, Long applicantId) {
        AccompanyMember applicant = findAndValidatePendingApplicant(ownerId, accompanyId, applicantId);

        applicant.updateAccompanyStatus(AccompanyRole.REJECTED);
    }

    private void validateOwner(Long memberId, Long accompanyId) {
        AccompanyMember accompanyMember = accompanyMemberRepository
                .findByMemberIdAndAccompanyId(memberId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("참여자가 아닙니다."));

        if (accompanyMember.getRole() != AccompanyRole.OWNER) {
            throw new AccompanyInvalidGroupException("생성자만 가능합니다.");
        }
    }

    private AccompanyMember findAndValidatePendingApplicant(Long ownerId, Long accompanyId, Long applicantId) {
        validateOwner(ownerId, accompanyId);

        AccompanyMember accompanyMember = accompanyMemberRepository
                .findByMemberIdAndAccompanyId(applicantId, accompanyId)
                .orElseThrow(() -> new AccompanyInvalidGroupException("신청 내역이 없습니다."));

        if (accompanyMember.getRole() != AccompanyRole.PENDING) {
            throw new AccompanyInvalidGroupException("이미 처리된 신청입니다.");
        }
        return accompanyMember;
    }

    private AccompanyListResDto getAccompanyListResDto(List<Accompany> accompanyList, Long memberId) {
        List<AccompanyMember> userMemberships = accompanyMemberRepository.findByMemberId(memberId);

        Map<Long, String> userRoleMap = userMemberships.stream()
                .collect(Collectors.toMap(
                        am -> am.getAccompany().getId(),
                        am -> am.getRole().name()
                ));

        List<AccompanyInfoResDto> accompanyInfoDtos = accompanyList.stream()
                .map(accompany -> {
                    String userRole = userRoleMap.get(accompany.getId());
                    return AccompanyInfoResDto.from(accompany, userRole);
                })
                .toList();

        return AccompanyListResDto.from(accompanyInfoDtos);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Accompany findAccompanyById(Long accompanyId) {
        return accompanyRepository.findById(accompanyId)
                .orElseThrow(() -> new AccompanyNotFoundException("동행 그룹을 찾을 수 없습니다."));
    }
}