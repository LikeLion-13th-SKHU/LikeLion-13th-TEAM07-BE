package com.example.ie_um.accompany.domain;

import com.example.ie_um.global.entity.BaseTimeEntity;
import com.example.ie_um.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccompanyMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accompany_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_id")
    private Accompany accompany;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AccompanyRole role;

    @Builder
    private AccompanyMember(Member member, Accompany accompany, AccompanyRole role) {
        this.member = member;
        this.accompany = accompany;
        this.role = role;
    }

    public void updateAccompanyStatus(AccompanyRole accompanyRole) {
        this.role = accompanyRole;
    }
}
