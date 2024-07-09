package net.nonworkspace.demo.service.job;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoTriggerListener implements TriggerListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        logger.debug("01. trigger is started");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        logger.debug("02. checking for job Exception ... ");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        logger.debug("03. trigger is not started");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
            CompletedExecutionInstruction triggerInstructionCode) {
        logger.debug("03. trigger is complete");
    }

}
