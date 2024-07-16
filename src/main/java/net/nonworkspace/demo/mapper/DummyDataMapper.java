package net.nonworkspace.demo.mapper;

import java.util.List;
import net.nonworkspace.demo.model.DummyDataVO;
import net.nonworkspace.demo.model.common.PagingVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface DummyDataMapper {

    public int insertDummyData(DummyDataVO dummyDataVO);

    public List<DummyDataVO> selectDummyDataList();

    public List<DummyDataVO> selectDummyDataPage(PagingVO pagingVO);

    public int selectDummyDataCount();
}
