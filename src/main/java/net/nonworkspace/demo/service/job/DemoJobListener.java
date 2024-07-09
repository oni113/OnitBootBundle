package net.nonworkspace.demo.service.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoJobListener implements JobListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public String getName() {
        return DemoJob.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.debug("Before Job Excuted");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.debug("Job has interruped! Job is Failed!");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.debug("After Job Excuted");
    }

}
