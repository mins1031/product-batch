package com.example.productbatch.intrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;

public record StarPointCalculateDto(
        long productId,
        long reviewId,
        String productName,
        double starPoint
) {

    @QueryProjection
    public StarPointCalculateDto(long productId, long reviewId, String productName, double starPoint) {
        this.productId = productId;
        this.reviewId = reviewId;
        this.productName = productName;
        this.starPoint = starPoint;
    }
}
