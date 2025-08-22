package com.example.ie_um.community.domain;

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
public class Community extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Community(String title, String content, String address, Member member) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.member = member;
    }

    public void update(String title, String content, String address) {
        this.title = title;
        this.content = content;
        this.address = address;
    }
}
