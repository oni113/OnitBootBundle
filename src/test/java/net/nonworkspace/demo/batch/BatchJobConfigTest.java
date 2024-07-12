package net.nonworkspace.demo.batch;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BatchJobConfigTest {

    @Autowired
    private BatchScheduler batchScheduler;

    @Test
    void runJobTest() {
        batchScheduler.runJob();
        Exception e = assertThrows(RuntimeException.class, () -> batchScheduler.runJob());
        assertNull(e);
    }
}