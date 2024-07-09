package net.nonworkspace.demo.service.job;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Job;

@Service
@RequiredArgsConstructor
public class JobService {
    public List<Job> getJobList() {
        return null;
    }

    public void runSchedule() throws SchedulerException {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key1", false);
        jobDataMap.put("key2", 123);
        jobDataMap.put("key3", "asbasdasd");

        // create Job
        JobDetail job = JobBuilder.newJob(DemoJob.class).withIdentity("demoJob", "group1")
                .withDescription("job test decription").setJobData(jobDataMap).build();

        // create Trigger
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("demoTrigger", "group1")
                .startNow().withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ?")).build();

        // crate Scheduler and register Job, Trigger
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        DemoJobListener listner = new DemoJobListener();
        scheduler.getListenerManager().addJobListener(listner);
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
