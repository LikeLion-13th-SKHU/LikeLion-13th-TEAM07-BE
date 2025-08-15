package com.example.ie_um.resource.domain;

import com.example.ie_um.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude", precision = 10, scale = 8) // DECIMAL(10,8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8) // DECIMAL(11,8) → 경도는 범위가 더 넓음
    private BigDecimal longitude;

    @Builder
    private Resource(String name, String description, String address, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
