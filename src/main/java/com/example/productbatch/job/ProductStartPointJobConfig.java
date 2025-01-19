package com.example.productbatch.job;

import com.example.productbatch.intrastructure.dto.StarPointCalculateDto;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;
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

    private ItemReader<StarPointCalculateDto> itemReader() {
        Map<String, Object> params = Map.of(
                "isCanView", true,
                "isCalculatedStarPoint", true
        );

        return new JpaPagingItemReaderBuilder<StarPointCalculateDto>()
                .name("StarPointCalculateDtoReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT p.id, r.id, p.name, r.starPoint " +
                        "FROM ProductEntity p join ReviewEntity r on p.id = r.productId " +
                        "WHERE p.isCanView = :isCanView amd r.isCalculatedStarPoint = :isCalculatedStarPoint " +
                        "ORDER BY p.id DESC"
                )
                .parameterValues(params)
                .pageSize(CHUNK_SIZE)
                .build()
                ;
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

//            collect.entrySet().stream()
//                    .map(entry ->
//                            {
//                                Long productId = entry.getKey();
//                                double totalStarPoint = entry.getValue().stream()
//                                        .mapToDouble(StarPointCalculateDto::starPoint)
//                                        .sum();
////                                totalStarPoint /
//                            }
//                    ).c();
//            starPointDslRepository.updateStartPointByProduct()

        };
    }
}
