package com.example.productbatch.job;

import com.example.productbatch.domain.Review;
import com.example.productbatch.intrastructure.dto.StarPointCalculateDto;
import com.example.productbatch.intrastructure.entity.ProductEntity;
import com.example.productbatch.intrastructure.entity.ReviewEntity;
import com.example.productbatch.intrastructure.repository.dsl.StarPointDslRepository;
import com.example.productbatch.intrastructure.repository.jpa.ProductJpaRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.support.CompositeItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProductStartPointJobConfig {
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager platformTransactionManager;
    private final StarPointDslRepository starPointDslRepository;

    private static final int CHUNK_SIZE = 500;

    @Bean
    public Job productStartPointJob(Step chunkFirstStep) {
        return new JobBuilder("productStartPointJob", jobRepository)
                .start(chunkFirstStep)
                .build();
    }

    @Bean
    @JobScope
    public Step chunkFirstStep() {
        return new StepBuilder("productStartPointStep", jobRepository)
                .<StarPointCalculateDto, StarPointCalculateDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(itemReader())
//                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    private ItemReader<StarPointCalculateDto> itemReader() throws Exception {
        List<ProductEntity> productEntities = readProductEntities();
        List<Long> productIds = productEntities.stream()
                .map(ProductEntity::getId)
                .toList();

        Map<String, Object> reviewParams = Map.of(
                "isCalculatedStarPoint", false,
                "productIds", productIds
        );

        JpaPagingItemReader<ReviewEntity> reviewResult = new JpaPagingItemReaderBuilder<ReviewEntity>()
                .name("reviewReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT r.id, r.statPoint" +
                                "FROM ReviewEntity r" +
                                "WHERE r.isCalculatedStarPoint = :isCalculatedStarPoint" +
                                    "and r.productId IN :productIds" +
                                "ORDER BY p.id DESC"
                )
                .parameterValues(reviewParams)
                .pageSize(CHUNK_SIZE)
                .build();

        Map<Long, List<ReviewEntity>> productReviewsMap = new HashMap<>();
        while (true) {
            ReviewEntity read = reviewResult.read();
            if (Objects.isNull(read)) {
                break;
            }

            long productId = read.getProductId();
            List<ReviewEntity> reviewList = productReviewsMap.getOrDefault(productId, new ArrayList<>());
            reviewList.add(read);

            productReviewsMap.put(productId, reviewList);
        }

        CompositeItemReader<StarPointCalculateDto> compositeItemReader = new CompositeItemReader<>(List);

        return StarPointCalculateDto.of(productReviewsMap);
    }

    private List<ProductEntity> readProductEntities() throws Exception {
        JpaPagingItemReader<ProductEntity> productResult = new JpaPagingItemReaderBuilder<ProductEntity>()
                .name("productReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT p.id, p.totalStartPoint, p.name " +
                                "FROM ProductEntity p" +
                                "WHERE p.isCanView = :isCanView" +
                                "ORDER BY p.id DESC"
                )
                .parameterValues(
                        Map.of(
                                "isCanView", true
                        )
                )
                .pageSize(CHUNK_SIZE)
                .build();

        List<ProductEntity> productEntities = new ArrayList<>();
        while (productResult.read() != null) {
            productEntities.add(productResult.read());
        }

        return productEntities;
    }

//    private ItemProcessor<? super StarPointCalculateDto, StarPointCalculateDto> itemProcessor() {
//
//        return null;
//    }

    private ItemWriter<StarPointCalculateDto> itemWriter() {
        return items -> {
            List<? extends StarPointCalculateDto> dtos = items.getItems();
            Map<Long, ? extends List<? extends StarPointCalculateDto>> collect =
                    dtos.stream().collect(groupingBy(StarPointCalculateDto::productId));

            // 

            starPointDslRepository.updateStartPointByProduct()

        };
    }
}
