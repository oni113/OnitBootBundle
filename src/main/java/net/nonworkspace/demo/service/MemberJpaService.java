package net.nonworkspace.demo.service;

import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.dto.common.ListResponse;
import net.nonworkspace.demo.domain.dto.member.MemberDto;
import net.nonworkspace.demo.domain.dto.member.MemberViewDto;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.domain.entity.Member;
import net.nonworkspace.demo.domain.entity.Password;
import net.nonworkspace.demo.domain.entity.Role;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;
import net.nonworkspace.demo.utils.StringUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberJpaService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    public ListResponse<MemberDto> findMembers(String name) {
        List<Member> members = (name == null || name.isEmpty()) ? memberRepository.findAllQuery()
                : memberRepository.findAllQuery(name);
        List<MemberDto> result = new ArrayList<>();
        members.forEach(m -> result.add(new MemberDto(m)));
        return new ListResponse<>(result);
    }

    public MemberViewDto findMember(Long memberId) {
        Member member = Optional.ofNullable(memberRepository.find(memberId))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER));
        return new MemberViewDto(member);
    }

    @Transactional
    public Long join(JoinRequestDto joinDto) {
        if (!StringUtil.isEmail(joinDto.email())) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_EMAIL_FORMAT);
        }
        validateDuplicateEmail(joinDto.email());

        if (!joinDto.password().equals(joinDto.rePassword())) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_INPUT_NOT_MATCHED);
        }

        Member member = Member.createJoinMember(joinDto);
        Long memberId = memberRepository.saveMember(member);

        member = memberRepository.find(memberId);
        Password password = Password.createPassword(member, encoder.encode(joinDto.password()));
        memberRepository.savePassword(password);

        Role role = Role.createRole(member, "USER");
        memberRepository.saveRole(role);

        return member.getMemberId();
    }

    @Transactional
    public MemberViewDto editMember(MemberViewDto member) {
        Member target = Optional.ofNullable(memberRepository.find(member.memberId()))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER));
        target.updateName(member.name());
        // memberRepository.saveMember(target); // 변경 감지
        return new MemberViewDto(target);
    }

    @Transactional
    public Long deleteMember(Long memberId) {
        Member target = Optional.ofNullable(memberRepository.find(memberId))
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER));
        memberRepository.delete(target.getMemberId());
        return 1L;
    }

    public ListResponse<MemberDto> getPage(String name, int pageNo, int pageSize) {
        Sort sort = Sort.by("createInfo.createDate").descending();
        Pageable pageable = PageRequest.of((pageNo - 1), pageSize, sort);   // pageNo : zero-based
        List<Member> members = memberRepository.findAllPageQuery(name, pageable);
        List<MemberDto> result = new ArrayList<>();
        members.forEach(m -> result.add(new MemberDto(m)));
        return new ListResponse<>(result);
    }

    private void validateDuplicateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE);
        });
    }
}
