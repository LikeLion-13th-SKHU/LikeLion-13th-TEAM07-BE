package com.example.ie_um.community.application;

import com.example.ie_um.community.api.dto.request.CommunityReqDto;
import com.example.ie_um.community.api.dto.response.CommunityInfoResDto;
import com.example.ie_um.community.api.dto.response.CommunityListResDto;
import com.example.ie_um.community.domain.Community;
import com.example.ie_um.community.domain.CommunityLike;
import com.example.ie_um.community.domain.repository.CommunityLikeRepository;
import com.example.ie_um.community.domain.repository.CommunityRepository;
import com.example.ie_um.community.exception.CommunityInvalidException;
import com.example.ie_um.community.exception.CommunityLikeNotFoundException;
import com.example.ie_um.community.exception.CommunityNotFoundException;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.domain.repository.MemberRepository;
import com.example.ie_um.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityLikeRepository communityLikeRepository;

    @Transactional
    public void create(Long memberId, CommunityReqDto communityReqDto) {
        Member member = findMemberById(memberId);
        Community community = Community.builder()
                .title(communityReqDto.title())
                .content(communityReqDto.content())
                .address(communityReqDto.address())
                .member(member)
                .likeCount(0)
                .build();

        communityRepository.save(community);
    }

    public CommunityInfoResDto getDetail(Long memberId, Long communityId) {
        Community community = findCommunityById(communityId);
        boolean isLikedByCurrentUser = communityLikeRepository.existsByMemberIdAndCommunityId(memberId, communityId);
        return CommunityInfoResDto.from(community, isLikedByCurrentUser);
    }

    public CommunityListResDto getAll(Long memberId) {
        List<Community> communities = communityRepository.findAll();
        return getCommunityListResDto(communities, memberId);
    }

    public CommunityListResDto getByMemberId(Long memberId) {
        List<Community> communities = communityRepository.findByMemberId(memberId);
        return getCommunityListResDto(communities, memberId);
    }

    public CommunityListResDto getByLike(Long memberId) {
        List<CommunityLike> communityLikesikes = communityLikeRepository.findByMemberIdWithCommunity(memberId);

        List<Community> communities = communityLikesikes.stream()
                .map(CommunityLike::getCommunity)
                .toList();

        return getCommunityListResDto(communities, memberId);
    }

    @Transactional
    public void update(Long memberId, Long communityId, CommunityReqDto communityReqDto) {
        Community community = validateOwner(memberId, communityId);
        community.update(
                communityReqDto.title(),
                communityReqDto.content(),
                communityReqDto.address());
    }

    @Transactional
    public void delete(Long memberId, Long communityId) {
        Community community = validateOwner(memberId, communityId);

        communityLikeRepository.deleteByCommunityId(communityId);
        communityRepository.delete(community);
    }

    @Transactional
    public void saveLike(Long memberId, Long communityId) {
        if (communityLikeRepository.findByMemberIdAndCommunityId(memberId, communityId).isPresent()) {
            throw new CommunityInvalidException("이미 좋아요를 누른 게시물입니다.");
        }

        Member member = findMemberById(memberId);
        Community community = findCommunityById(communityId);

        CommunityLike communityLike = CommunityLike.builder()
                .member(member)
                .community(community)
                .build();

        community.increasedLikeCount();
        communityLikeRepository.save(communityLike);
    }

    @Transactional
    public void deleteLike(Long memberId, Long communityId) {
        CommunityLike communityLike = communityLikeRepository.findByMemberIdAndCommunityId(memberId, communityId)
                .orElseThrow(() -> new CommunityLikeNotFoundException("좋아요 기록을 찾을 수 없습니다."));

        Community community = communityLike.getCommunity();
        community.decreasedLikeCount();
        communityLikeRepository.delete(communityLike);
    }

    private CommunityListResDto getCommunityListResDto(List<Community> communityList, Long memberId) {
        Set<Long> likedCommunityIds = communityLikeRepository.findByMemberId(memberId).stream()
                .map(like -> like.getCommunity().getId())
                .collect(Collectors.toSet());

        List<CommunityInfoResDto> communityInfoDtos = communityList.stream()
                .map(community -> {
                    boolean isLike = likedCommunityIds.contains(community.getId());
                    return CommunityInfoResDto.from(community, isLike);
                })
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
