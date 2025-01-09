package com.example.productbatch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SampleTestJobConfiguration {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;

    @Bean
    public Job sampleMinTestJob(Step firstStep, Step secondStep) {
        return new JobBuilder("simpleTestJob", jobRepository)
                .start(firstStep)
                .next(secondStep)
                .build();
    }

    @Bean
    @JobScope
    public Step firstStep() {
        return new StepBuilder("simpleTestFirstJob", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("simpleTestFirstJob!!");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step secondStep() {
        return new StepBuilder("simpleTestSecondJob", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("simpleTestSecondJob!!");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

}
