package net.nonworkspace.demo.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import net.nonworkspace.demo.service.job.DemoJob;
import net.nonworkspace.demo.service.job.DemoJobListener;
import net.nonworkspace.demo.service.job.DemoTriggerListener;

@Configuration
public class BatchSchedulerConfig {

    @Value("${batch.job.schedule}")
    private String batchJobSchedule;

    private Scheduler scheduler;

    public BatchSchedulerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    private void jobProgress() throws SchedulerException {
        // cronScheduler();
    }

    private void cronScheduler() throws SchedulerException {
        // job 생성
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("field1", true);
        jobDataMap.put("field2", 17883939);
        jobDataMap.put("field3", "lkaskdjklajsdklj");

        JobDetail job = JobBuilder.newJob()
                .ofType(DemoJob.class)
                .withIdentity("job1", "jobGroup")
                .withDescription("demo job description")
                .setJobData(jobDataMap)
                .usingJobData("field4", "44444")
                .build();

        // trigger 생성
        CronTrigger cronTrigger =
                TriggerBuilder.newTrigger()
                        .withIdentity("trigger1", "triggerGroup")
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(batchJobSchedule)).build();

        // scheduler 생성, job/trigger 등록
        scheduler = new StdSchedulerFactory().getScheduler();
        DemoJobListener jobListener = new DemoJobListener();
        DemoTriggerListener triggerListener = new DemoTriggerListener();
        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.getListenerManager().addTriggerListener(triggerListener);
        scheduler.start();
        scheduler.scheduleJob(job, cronTrigger);
    }
}
