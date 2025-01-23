package com.example.productbatch.intrastructure.repository.dsl;

import com.example.productbatch.intrastructure.dto.QStarPointCalculateDto;
import com.example.productbatch.intrastructure.dto.StarPointCalculateDto;
import com.example.productbatch.intrastructure.entity.QProductEntity;
import com.example.productbatch.intrastructure.entity.QReviewEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.productbatch.intrastructure.entity.QProductEntity.productEntity;
import static com.example.productbatch.intrastructure.entity.QReviewEntity.reviewEntity;

@Repository
@RequiredArgsConstructor
public class StarPointDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateStartPointByProduct(Long productId, double totalStartPoint) {
        jpaQueryFactory
                .update(productEntity)
                .set(productEntity.totalStartPoint, totalStartPoint)
                .where(productEntity.id.eq(productId));
    }

//    @Transactional
//    public List<StarPointCalculateDto> findProductAndReviewInCalculateTarget(Pageable pageable) {
//        return jpaQueryFactory
//                .select(
//                        new QStarPointCalculateDto(
//                                productEntity.id,
//                                reviewEntity.id,
//                                productEntity.name,
//                                reviewEntity.startPoint
//                        )
//                )
//                .from(productEntity, reviewEntity)
//                .where(
//                        productEntity.isCanView.isTrue()
//                                .and(reviewEntity.isCalculatedStarPoint).isTrue()
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch()
//                ;
//    }
}
