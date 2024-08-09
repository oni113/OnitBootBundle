package net.nonworkspace.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Password;
import net.nonworkspace.demo.domain.Role;
import net.nonworkspace.demo.domain.dto.member.MemberDto;
import net.nonworkspace.demo.domain.dto.member.MemberViewDto;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberJpaService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    public List<Member> findMembers(String name) {
        return (name == null || name.isEmpty()) ? memberRepository.findAll()
            : memberRepository.findAll(name);
    }

    public MemberViewDto findMember(Long memberId) {
        Member member = Optional.ofNullable(memberRepository.find(memberId))
            .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER));
        return new MemberViewDto(member);
    }

    @Transactional
    public Long join(JoinRequestDto joinDto) {
        Member member = new Member();
        member.setEmail(joinDto.email());
        validateDuplicateEmail(member);

        if (!StringUtil.isEmail(joinDto.email())) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_EMAIL_FORMAT);
        }

        if (!joinDto.password().equals(joinDto.rePassword())) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_INPUT_NOT_MATCHED);
        }

        member.setName(joinDto.name());
        Long memberId = memberRepository.saveMember(member);

        member = memberRepository.find(memberId);
        Password password = Password.createPassword(member, encoder.encode(joinDto.password()));
        memberRepository.savePassword(password);

        Role role = Role.createRole(member, "USER");
        memberRepository.saveRole(role);

        return member.getMemberId();
    }

    @Transactional
    public Member editMember(Member member) {
        Member target = memberRepository.find(member.getMemberId());
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        target.setName(member.getName());
        memberRepository.saveMember(target);

        return target;
    }

    @Transactional
    public Long deleteMember(Long memberId) {
        Member target = memberRepository.find(memberId);
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER);
        }
        memberRepository.delete(target.getMemberId());
        return 1L;
    }

    private void validateDuplicateEmail(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
            throw new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE);
        });
    }

    public List<MemberDto> getPage(String name, int pageNo, int pageSize) {
        Sort sort = Sort.by("createInfo.createDate").descending();
        Pageable pageable = PageRequest.of((pageNo - 1), pageSize, sort);   // pageNo : zero-based
        List<Member> members = memberRepository.findAll(name, pageable);
        List<MemberDto> result = new ArrayList<>();
        members.forEach(m -> result.add(new MemberDto(m)));
        return result;
    }
}
