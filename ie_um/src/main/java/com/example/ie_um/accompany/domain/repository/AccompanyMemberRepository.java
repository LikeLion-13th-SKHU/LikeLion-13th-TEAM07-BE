package com.example.ie_um.accompany.domain.repository;

import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.accompany.domain.AccompanyRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccompanyMemberRepository extends JpaRepository<AccompanyMember, Long> {
    List<AccompanyMember> findByMemberId(Long memberId);

    boolean existsByMemberIdAndAccompany(Long memberId, Accompany accompany);

    void deleteByAccompanyId(Long accompanyId);

    Optional<AccompanyMember> findByMemberIdAndAccompanyId(Long memberId, Long accompanyId);

    Optional<AccompanyMember> findByMemberIdAndAccompany(Long memberId, Accompany accompany);

    List<AccompanyMember> findByMemberIdAndRole(Long memberId, AccompanyRole accompanyRole);

    List<AccompanyMember> findByMemberIdAndRoleIn(Long memberId, List<AccompanyRole> statuses);
}
