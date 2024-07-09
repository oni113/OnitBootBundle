package net.nonworkspace.demo.batch;

import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.service.job.JobService;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class DemoBatchJobSchedulerTest {
    
    @Autowired
    private JobService jobSerivce;
    
    @Test
    public void runSchedule() throws SchedulerException {
        jobSerivce.runSchedule();
    }
}
