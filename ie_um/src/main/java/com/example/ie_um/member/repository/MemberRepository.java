package com.example.ie_um.member.repository;

import java.util.Optional;
import com.example.ie_um.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
