package com.example.ie_um.community.domain.repository;

import com.example.ie_um.community.domain.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    Optional<CommunityLike> findByMemberIdAndCommunityId(Long memberId, Long communityId);

    void deleteByMemberIdAndCommunityId(Long memberId, Long communityId);

    List<CommunityLike> findByMemberId(Long memberId);

    Optional<CommunityLike> findByCommunityId(Long communityId);

    boolean existsByMemberIdAndCommunityId(Long memberId, Long communityId);

    @Query("SELECT cl FROM CommunityLike cl JOIN FETCH cl.community WHERE cl.member.id = :memberId")
    List<CommunityLike> findByMemberIdWithCommunity(@Param("memberId") Long memberId);

    void deleteByCommunityId(Long communityId);
}
