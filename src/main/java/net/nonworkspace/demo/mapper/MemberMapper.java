package net.nonworkspace.demo.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import net.nonworkspace.demo.model.MemberVO;

@Mapper
public interface MemberMapper {

    public List<MemberVO> selectMemberList(MemberVO member);

    public Optional<MemberVO> findByEmail(String email);

    public int insertMember(MemberVO member);

    public MemberVO findById(Long memberId);

    public int updateMember(MemberVO member);

    public int deleteMember(Long memberId);

    public int insertMemberPassword(MemberVO member);
}
