package com.example.ie_um.community.application;

import com.example.ie_um.community.api.dto.request.CommunityCreateReqDto;
import com.example.ie_um.community.api.dto.request.CommunityUpdateReqDto;
import com.example.ie_um.community.api.dto.response.CommunityInfoResDto;
import com.example.ie_um.community.api.dto.response.CommunityListResDto;
import com.example.ie_um.community.domain.Community;
import com.example.ie_um.community.domain.CommunityMember;
import com.example.ie_um.community.domain.repository.CommunityMemberRepository;
import com.example.ie_um.community.domain.repository.CommunityRepository;
import com.example.ie_um.community.exception.CommunityInvalidException;
import com.example.ie_um.community.exception.CommunityNotFoundException;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.exception.MemberNotFoundException;
import com.example.ie_um.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;

    @Transactional
    public void create(Long memberId, CommunityCreateReqDto dto) {
        Member member = findMemberById(memberId);
        Community community = Community.builder()
                .title(dto.title())
                .content(dto.content())
                .address(dto.address())
                .build();

        CommunityMember communityMember = CommunityMember.builder()
                .member(member)
                .community(community)
                .build();

        communityRepository.save(community);
        communityMemberRepository.save(communityMember);
    }

    public CommunityInfoResDto getDetail(Long memberId, Long communityId) {
        Community community = findCommunityById(communityId);
        return CommunityInfoResDto.from(community);
    }

    public CommunityListResDto getAll() {
        List<Community> communities = communityRepository.findAll();
        return getCommunityListResDto(communities);
    }

    public CommunityListResDto getByMemberId(Long memberId) {
        List<CommunityMember> communityMembers = communityMemberRepository.findByMemberId(memberId);

        List<Community> communities = communityMembers.stream()
                .map(CommunityMember::getCommunity)
                .toList();

        return getCommunityListResDto(communities);
    }

    @Transactional
    public void update(Long memberId, Long communityId, CommunityUpdateReqDto communityUpdateReqDto) {
        validateOwner(memberId, communityId);
        Community community = findCommunityById(communityId);
        community.update(communityUpdateReqDto.title(),
                communityUpdateReqDto.content(),
                communityUpdateReqDto.address());
    }

    @Transactional
    public void delete(Long memberId, Long communityId) {
        validateOwner(memberId, communityId);

        Community community = findCommunityById(communityId);

        CommunityMember communityMember = communityMemberRepository.findByMemberIdAndCommunityId(memberId, communityId)
                .orElseThrow(() -> new CommunityNotFoundException("존재하지 않는 게시물입니다."));

        communityMemberRepository.delete(communityMember);
        communityRepository.delete(community);
    }

    private CommunityListResDto getCommunityListResDto(List<Community> communityList) {
        List<CommunityInfoResDto> communityInfoDtos = communityList.stream()
                .map(CommunityInfoResDto::from)
                .toList();
        return CommunityListResDto.from(communityInfoDtos);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));
    }

    private Community findCommunityById(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("존재하지 않는 게시물입니다."));
    }

    private void validateOwner(Long memberId, Long communityId) {
        CommunityMember communityMember = communityMemberRepository.findByMemberIdAndCommunityId(memberId, communityId)
                .orElseThrow(() -> new CommunityNotFoundException("존재하지 않는 게시물입니다."));
        if (communityMember.getMember().getId() != memberId)
            throw new CommunityInvalidException("생성자만 수정/삭제가 가능합니다.");
    }
}
