package net.nonworkspace.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.mapper.DummyDataMapper;
import net.nonworkspace.demo.model.DummyDataVO;
import net.nonworkspace.demo.model.common.PagingVO;
import net.nonworkspace.demo.utils.StringUtil;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DummyDataService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DummyDataMapper dummyDataMapper;

    private final SqlSessionFactory sqlSessionFactory;

    public int[] createDummyDataOneMillionRowsBatchType() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        DummyDataMapper batchTypeMapper = sqlSession.getMapper(DummyDataMapper.class);

        DummyDataVO dummy = null;

        for (int i = 0; i < 100000; i++) {
            dummy = new DummyDataVO();
            dummy.setStringValue(StringUtil.getRandomStringValue(10));
            dummy.setNumberValue((int) (Math.random() * 99999) + 1);
            batchTypeMapper.insertDummyData(dummy);
        }

        List<BatchResult> batchResults = sqlSession.flushStatements();
        return batchResults.get(0).getUpdateCounts();
    }

    public int createDummyDataOneMillionRowsSimpleType() throws Exception {
        int rowCount = 0;

        DummyDataVO dummy = null;

        for (int i = 0; i < 100000; i++) {
            dummy = new DummyDataVO();
            dummy.setStringValue(StringUtil.getRandomStringValue(10));
            dummy.setNumberValue((int) (Math.random() * 99999) + 1);
            rowCount += dummyDataMapper.insertDummyData(dummy);
        }

        return rowCount;
    }

    public List<DummyDataVO> getDummyDataPage(int pageNo, int pageSize) throws Exception {
        Sort sort = Sort.by("dummyId").ascending();
        PagingVO pageVO = new PagingVO(pageNo, pageSize, sort);
        List<DummyDataVO> dummyDataVOPage = dummyDataMapper.selectDummyDataPage(pageVO);
        return dummyDataVOPage;
    }
}
