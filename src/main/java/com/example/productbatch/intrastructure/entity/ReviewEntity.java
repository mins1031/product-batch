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
@Table(name = "review")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long reviewerId;
    private long orderId;
    private long productId;
    private double startPoint;

    @Builder
    public ReviewEntity(long reviewerId, long orderId, long productId, double startPoint) {
        this.reviewerId = reviewerId;
        this.orderId = orderId;
        this.productId = productId;
        this.startPoint = startPoint;
    }
}
