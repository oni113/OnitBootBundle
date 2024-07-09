package net.nonworkspace.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DummyDataServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DummyDataService dummyDataService;

    @Test
    // @Rollback(false)
    void testInsertBatchType() throws Exception {

        for (int updateCount : dummyDataService.createDummyDataOneMillionRowsBatchType()) {
            assertEquals(1, updateCount);
        }
    }

    @Test
    void testInsertSimpleType() throws Exception {

        assertEquals(100000, dummyDataService.createDummyDataOneMillionRowsSimpleType());
    }
}
