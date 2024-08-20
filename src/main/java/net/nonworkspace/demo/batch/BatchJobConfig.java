package net.nonworkspace.demo.batch;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.model.DummyDataVO;
import net.nonworkspace.demo.service.DummyDataService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchJobConfig extends DefaultBatchConfiguration {

    private final DummyDataService dummyDataService;

    @Bean
    public Job firstJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("firstJob", jobRepository)
            // TODO : How to get parameter from BatchScheduler.jobParams
            .start(firstStep(jobRepository, transactionManager))
            // .next(secondStep(jobRepository, transactionManager))
            .build();
    }

    @Bean
    public Step firstStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("firstStep", jobRepository)
            .<DummyDataVO, DummyDataVO>chunk(100000, transactionManager)
            .reader(firstChunkReader(jobRepository))
            .processor(firstChunkProcessor())
            .writer(firstChunkWriter())
            // .tasklet(firstTasklet(), transactionManager)
            .build();
    }

    private ItemProcessor<DummyDataVO, DummyDataVO> firstChunkProcessor() {
        return dummyData -> dummyData;
    }

    private ItemReader<DummyDataVO> firstChunkReader(JobRepository jobRepository) throws Exception {
        List<DummyDataVO> result = dummyDataService.getDummyDataPage(0, 100000);
        return new ListItemReader<>(result);
    }

    private ItemWriter<DummyDataVO> firstChunkWriter() throws Exception {
        log.info("first chunk execute!!");
        return (dummyData) -> {
            dummyData.getItems().stream().forEach(d -> {
                log.info("No. {} dummyData string value: {}", d.getDummyId(), d.getStringValue());
            });
        };
    }

    @Bean
    public Step secondStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("secondStep", jobRepository)
            .<Integer, Integer>chunk(1, transactionManager)
            .reader(secondChunkReader())
            .writer(secondChunkWriter())
            // .tasklet(secondTasklet(), transactionManager)
            .build();
    }

    private ItemReader<Integer> secondChunkReader() {
        Integer[] array = {1, 2, 3, 4};
        return new ListItemReader<>(Arrays.asList(array));
    }

    private ItemWriter<Integer> secondChunkWriter() {
        log.info("second chunk execute!!");
        return (integers) -> {
        };
    }

    @Bean
    public Tasklet firstTasklet() {
        return ((contribution, chunkContent) -> {
            log.info("first tasklet execute!!");
            // dummyDataService.createDummyDataOneMillionRowsSimpleType();
            dummyDataService.createDummyDataOneMillionRowsBatchType();
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
