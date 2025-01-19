package com.example.productbatch.intrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "product")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isCanView;
    private double totalStartPoint;

    @Builder
    public ProductEntity(String name, boolean isCanView, double totalStartPoint) {
        this.name = name;
        this.isCanView = isCanView;
        this.totalStartPoint = totalStartPoint;
    }
}
