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
public class SampleTestJobConfig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;

    private int testInt1 = 0;
    private int testInt2 = 0;

    @Bean
    public Job sampleMinTestJob(Step firstStep, Step secondStep) {
        return new JobBuilder("simpleTestJob3", jobRepository)
                .start(firstStep)
                .next(secondStep)
                .build();
    }

    @Bean
    @JobScope
    public Step firstStep() {
        return new StepBuilder("simpleTestFirstJob", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    if (testInt1 == 5) {
                        System.out.println("simpleTestFirstJob FINISHED!! : " + testInt1);
                        return RepeatStatus.FINISHED;
                    }

                    System.out.println("simpleTestFirstJob!! : " + testInt1);
                    testInt1++;
                    return RepeatStatus.CONTINUABLE;
                }, platformTransactionManager)
//                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public Step secondStep() {
        return new StepBuilder("simpleTestSecondJob", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    if (testInt2 == 5) {
                        System.out.println("simpleTestSecondJob FINISHED!! : " + testInt2);
                        return RepeatStatus.FINISHED;
                    }

                    System.out.println("simpleTestSecondJob!! : " + testInt2);
                    testInt2++;
                    return RepeatStatus.CONTINUABLE;
                }, platformTransactionManager).allowStartIfComplete(false)
                .build();
    }

}
