package com.example.ie_um.accompany.domain.repository;

import com.example.ie_um.accompany.domain.Accompany;
import com.example.ie_um.accompany.domain.AccompanyMember;
import com.example.ie_um.accompany.domain.AccompanyRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccompanyMemberRepository extends JpaRepository<AccompanyMember, Long> {
    boolean existsByMemberIdAndAccompany(Long memberId, Accompany accompany);

    void deleteByAccompanyId(Long accompanyId);

    Optional<AccompanyMember> findByMemberIdAndAccompanyId(Long memberId, Long accompanyId);

    Optional<AccompanyMember> findByMemberIdAndAccompany(Long memberId, Accompany accompany);

    List<AccompanyMember> findByMemberIdAndRoleIn(Long memberId, List<AccompanyRole> statuses);

    List<AccompanyMember> findByMemberId(Long memberId);

    @Query("SELECT am FROM AccompanyMember am JOIN FETCH am.member WHERE am.accompany.id = :accompanyId AND am.role = :role")
    List<AccompanyMember> findByAccompanyIdAndRoleWithMember(@Param("accompanyId") Long accompanyId,
                                                             @Param("role") AccompanyRole role);
}
