package com.example.productbatch.intrastructure.dto;

import com.example.productbatch.intrastructure.entity.ReviewEntity;

import java.util.List;
import java.util.Map;

public record StarPointCalculateDto(
        Map<Long, List<ReviewEntity>> productReviewMap
) {
    public static StarPointCalculateDto of(Map<Long, List<ReviewEntity>> productReviewMap) {
        return new StarPointCalculateDto(productReviewMap);
    }
}
