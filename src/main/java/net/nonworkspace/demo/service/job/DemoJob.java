package net.nonworkspace.demo.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // throw new JobExecutionException("Demo Job 에러 발생!!");

        log.debug("Job is running!!");

        context.getMergedJobDataMap().forEach((key, value) -> {
            log.debug("data map {}: {}", key, value);
        });
    }

}
