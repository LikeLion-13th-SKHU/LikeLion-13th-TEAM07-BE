package com.example.ie_um.community.application;

import com.example.ie_um.community.api.dto.request.CommunityCreateReqDto;
import com.example.ie_um.community.api.dto.request.CommunityUpdateReqDto;
import com.example.ie_um.community.api.dto.response.CommunityInfoResDto;
import com.example.ie_um.community.api.dto.response.CommunityListResDto;
import com.example.ie_um.community.domain.Community;
import com.example.ie_um.community.domain.CommunityLike;
import com.example.ie_um.community.domain.repository.CommunityLikeRepository;
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
    private final CommunityLikeRepository communityLikeRepository;

    @Transactional
    public void create(Long memberId, CommunityCreateReqDto dto) {
        Member member = findMemberById(memberId);
        Community community = Community.builder()
                .title(dto.title())
                .content(dto.content())
                .address(dto.address())
                .member(member)
                .build();

        communityRepository.save(community);
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
        List<Community> communities = communityRepository.findByMemberId(memberId);
        return getCommunityListResDto(communities);
    }

    public CommunityListResDto getByLike(Long memberId) {
        List<CommunityLike> likes = communityLikeRepository.findByMemberId(memberId);

        List<Community> communities = likes.stream()
                .map(CommunityLike::getCommunity)
                .toList();

        return getCommunityListResDto(communities);
    }


    @Transactional
    public void update(Long memberId, Long communityId, CommunityUpdateReqDto dto) {
        Community community = validateOwner(memberId, communityId);
        community.update(dto.title(), dto.content(), dto.address());
    }

    @Transactional
    public void delete(Long memberId, Long communityId) {
        Community community = validateOwner(memberId, communityId);
        communityRepository.delete(community);
    }

    @Transactional
    public void saveLike(Long memberId, Long communityId) {
        if (communityLikeRepository.findByMemberIdAndCommunityId(memberId, communityId).isPresent()) {
            return;
        }

        Member member = findMemberById(memberId);
        Community community = findCommunityById(communityId);

        CommunityLike communityLike = CommunityLike.builder()
                .member(member)
                .community(community)
                .build();

        communityLikeRepository.save(communityLike);
    }

    @Transactional
    public void deleteLike(Long memberId, Long communityId) {
        communityLikeRepository.deleteByMemberIdAndCommunityId(memberId, communityId);
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

    private Community validateOwner(Long memberId, Long communityId) {
        Community community = findCommunityById(communityId);
        if (!community.getMember().getId().equals(memberId)) {
            throw new CommunityInvalidException("생성자만 수정/삭제할 수 있습니다.");
        }
        return community;
    }
}
