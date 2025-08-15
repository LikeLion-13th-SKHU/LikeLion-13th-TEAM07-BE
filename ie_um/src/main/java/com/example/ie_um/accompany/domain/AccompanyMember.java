package com.example.ie_um.accompany.domain;

import com.example.ie_um.domain.member.entity.Member;
import com.example.ie_um.global.entity.BaseTimeEntity;
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

    @Column(name = "is_owner")
    private boolean isOwner;

    @Enumerated(EnumType.STRING)
    @Column(name = "accompany_status")
    private AccompanyStatus accompanyStatus;

    @Builder
    private AccompanyMember(Member member, Accompany accompany, boolean isOwner, AccompanyStatus accompanyStatus) {
        this.member = member;
        this.accompany = accompany;
        this.isOwner = isOwner;
        this.accompanyStatus = accompanyStatus;
    }

    public void updateAccompanyStatus(AccompanyStatus accompanyStatus) {
        this.accompanyStatus = accompanyStatus;
    }
}
