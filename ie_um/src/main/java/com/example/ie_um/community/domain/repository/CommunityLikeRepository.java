package com.example.ie_um.community.domain.repository;

import com.example.ie_um.community.domain.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    Optional<CommunityLike> findByMemberIdAndCommunityId(Long memberId, Long communityId);

    void deleteByMemberIdAndCommunityId(Long memberId, Long communityId);

    List<CommunityLike> findByMemberId(Long memberId);
}
