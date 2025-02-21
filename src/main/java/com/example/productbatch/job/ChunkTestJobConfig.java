package com.example.productbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChunkTestJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job chunkTestJob(Step chunkFirstStep) {
        return new JobBuilder("ChunkTestJob5", jobRepository)
                .start(chunkFirstStep)
                .build();
    }

    @Bean
    @JobScope
    public Step chunkFirstStep() {
        AtomicInteger count = new AtomicInteger(0);
        return new StepBuilder("ChunkTestStep", jobRepository)
                .chunk(10, platformTransactionManager)
                .reader(() -> {
                    log.info("reader Test");
                    if (count.get() == 10) {
                        log.info("reader finish!");
                        return null;
                    }

                    return true;
                })
                .processor(item -> {
                    int newCount = count.get() + 1;
                    count.set(newCount);
                    log.info("item is True? : {}", item);
                    return item;
                })
                .writer(item -> {
                    log.info("final status : {}", item);
                })
                .build();
    }
}
