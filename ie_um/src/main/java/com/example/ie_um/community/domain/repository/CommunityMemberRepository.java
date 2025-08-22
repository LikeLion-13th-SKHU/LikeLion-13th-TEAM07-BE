package com.example.ie_um.community.domain.repository;

import com.example.ie_um.community.domain.Community;
import com.example.ie_um.community.domain.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    Optional<CommunityMember> findByMemberIdAndCommunityId(Long memberId, Long communityId);

    List<CommunityMember> findByMemberId(Long memberId);

}
