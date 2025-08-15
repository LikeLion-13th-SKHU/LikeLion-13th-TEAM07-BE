package com.example.ie_um.accompany.domain;

import com.example.ie_um.global.entity.BaseTimeEntity;
import com.example.ie_um.resource.domain.Resource;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accompany extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accompany_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "max_personnel")
    private int maxPersonnel;

    @Column(name = "current_personnel")
    private int currentPersonnel;

    @Column(name = "time")
    private String time;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id")
    private Resource place;

    @Builder
    private Accompany(String title, String content, int maxPersonnel, int currentPersonnel, String time, Resource place) {
        this.title = title;
        this.content = content;
        this.maxPersonnel = maxPersonnel;
        this.currentPersonnel = currentPersonnel;
        this.time = time;
        this.place = place;
    }

    public void update(String title, String content, int maxPersonnel, int currentPersonnel, String time) {
        this.title = title;
        this.content = content;
        this.maxPersonnel = maxPersonnel;
        this.currentPersonnel = currentPersonnel;
        this.time = time;
    }

    public void increaseCurrentPersonnel() {
        currentPersonnel++;
    }

    public void decreaseCurrentPersonnel() {
        currentPersonnel--;
    }
}
