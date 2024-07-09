package net.nonworkspace.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SimpleJobConfig {
    @Bean
    public Job simpleJob1(JobRepository jobRepository, Step simpleStep1) {
        return new JobBuilder("simpleJob", jobRepository).start(simpleStep1).build();
    }

    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet1,
            PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet1, platformTransactionManager).build();
    }
    
    @Bean
    public Step simpleStep2(JobRepository jobRepository, Tasklet testTasklet2,
            PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet(testTasklet2, platformTransactionManager).build();
    }

    @Bean
    public Tasklet testTasklet1() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>> This is Step1");
            return RepeatStatus.FINISHED;
        });
    }
    
    @Bean
    public Tasklet testTasklet2() {
        return ((contribution, chunkContext) -> {
            log.info(">>>>> This is Step2");
            return RepeatStatus.FINISHED;
        });
    }
}
