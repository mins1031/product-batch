package com.example.productbatch.executer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStarPointExecutor {
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul") // 초 분 시 일 월 요일
    public void calculatedProductStarPoint() {
        Map<String, JobParameter<?>> hashMap = new HashMap<>();
        hashMap.put("time", new JobParameter<>(System.currentTimeMillis(), Long.class));
        JobParameters jobParameters = new JobParameters(hashMap);

//        jobLauncher.run()
    }
}
