package net.nonworkspace.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.nonworkspace.demo.model.DummyDataVO;

@Mapper
public interface DummyDataMapper {

    public int insertDummyData(DummyDataVO dummyDataVO);
    
    public List<DummyDataVO> selectDummyDataList();
}
