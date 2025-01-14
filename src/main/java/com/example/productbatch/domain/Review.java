package com.example.productbatch.domain;

import lombok.Getter;

@Getter
public class Review {
    private Long id;
    private long reviewerId;
    private long orderId;
    private long productId;
    private double startPoint;

    public Review(long reviewerId, long orderId, long productId, double startPoint) {
        this.reviewerId = reviewerId;
        this.orderId = orderId;
        this.productId = productId;
        this.startPoint = startPoint;
    }
}
