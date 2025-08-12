package com.example.ie_um.accompany.domain.repository;

import com.example.ie_um.accompany.domain.AccompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccompanyMemberRepository extends JpaRepository<AccompanyMember, Long> {
    void deleteByAccompanyId(Long accompanyId);

    Optional<AccompanyMember> findByMemberIdAndAccompanyId(Long memberId, Long accompanyId);
}
