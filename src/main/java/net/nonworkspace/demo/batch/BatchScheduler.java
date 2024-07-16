package net.nonworkspace.demo.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "batch.scheduling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BatchScheduler {

    private final JobLauncher jobLauncher;

    private final JobRegistry jobRegistry;

    @Scheduled(cron = "${batch.job.schedule}")
    public void runJob() {
        String time = LocalDateTime.now().toString();

        try {
            Job job = jobRegistry.getJob("firstJob");
            JobParametersBuilder jobParams = new JobParametersBuilder()
                .addString("time", time)
                .addString("name", "한글 파라미터 값");
            jobLauncher.run(job, jobParams.toJobParameters());
        } catch (NoSuchJobException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}
