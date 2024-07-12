package net.nonworkspace.demo.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job firstJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new JobBuilder("firstJob", jobRepository)
            .start(firstStep(jobRepository, transactionManager))
            .next(secondStep(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step firstStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("firstStep", jobRepository)
            .tasklet(firstTasklet(), transactionManager)
            .build();
    }

    @Bean
    public Step secondStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("secondStep", jobRepository)
            .tasklet(secondTasklet(), transactionManager)
            .build();
    }

    @Bean
    public Tasklet firstTasklet() {
        return ((contribution, chunkContent) -> {
            log.info("first tasklet execute!!");
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Tasklet secondTasklet() {
        return ((contribution, chunkContent) -> {
            log.info("second tasklet execute!!");
            return RepeatStatus.FINISHED;
        });
    }
}
