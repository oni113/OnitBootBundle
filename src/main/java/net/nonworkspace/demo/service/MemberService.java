package net.nonworkspace.demo.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.mapper.MemberMapper;
import net.nonworkspace.demo.model.MemberVO;
import net.nonworkspace.demo.utils.PasswordUtil;
import net.nonworkspace.demo.utils.StringUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;

    private final BCryptPasswordEncoder encoder;

    public List<MemberVO> findMembers(String name) {
        MemberVO condition = new MemberVO();
        condition.setName(name);
        List<MemberVO> result = memberMapper.selectMemberList(condition);
        result.parallelStream().forEach(m -> log.debug("findMembers : {}", m.toString()));

        return result;
    }

    @Transactional
    public Long join(MemberVO memberVO) {
        validateInput(memberVO);
        validateDuplicateEmail(memberVO);
        int result = memberMapper.insertMember(memberVO);
        if (result == 1) {
            memberVO.setMemberPassword(encoder.encode(memberVO.getMemberPassword()));
            memberMapper.insertMemberPassword(memberVO);
        }
        return memberVO.getMemberId();
    }

    public MemberVO findMember(Long memberId) {
        MemberVO member = Optional.ofNullable(memberMapper.findById(memberId))
            .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        return member;
    }

    @Transactional
    public MemberVO editMember(MemberVO memberVO) {
        int result = memberMapper.updateMember(memberVO);
        if (result <= 0) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        return memberMapper.findById(memberVO.getMemberId());
    }

    @Transactional
    public int deleteMember(Long memberId) {
        int result = memberMapper.deleteMember(memberId);
        if (result <= 0) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        return result;
    }

    private void validateDuplicateEmail(MemberVO memberVO) {
        memberMapper.findByEmail(memberVO.getEmail()).ifPresent(m -> {
            throw new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE);
        });
    }

    private void validateInput(MemberVO memberVO) {
        if (!StringUtil.isEmail(memberVO.getEmail())) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_EMAIL_FORMAT);
        }
        if (!PasswordUtil.isPassword(memberVO.getMemberPassword())) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
