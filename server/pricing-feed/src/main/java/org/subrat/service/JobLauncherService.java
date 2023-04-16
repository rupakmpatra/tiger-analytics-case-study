package org.subrat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class JobLauncherService {
    private final JobLauncher jobLauncher;
    private final Job pricingFeedJob;

    public String lauchJob(MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Path tempFile = Files.createTempFile("pricing-feed", ".csv");
        file.transferTo(tempFile);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("fileName", file.getOriginalFilename())
                .addString("input.file.loader", "resourceLoader")
                .addString("input.file.path", tempFile.toString())
                .toJobParameters();
        jobLauncher.run(pricingFeedJob, jobParameters);
        Files.delete(tempFile);
        return "Pricing feed import job completed.";
    }
}
