package net.nonworkspace.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import net.nonworkspace.demo.domain.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Password;
import net.nonworkspace.demo.domain.dto.JoinRequestDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;
import net.nonworkspace.demo.utils.PasswordUtil;

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

    public Member findMember(Long memberId) {
        Member member = memberRepository.find(memberId);
        if (member == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        return member;
    }

    @Transactional
    public Long join(JoinRequestDto joinDto) {
        Member member = new Member();
        member.setEmail(joinDto.getEmail());
        validateDuplicateEmail(member);

        if (!joinDto.getPassword().equals(joinDto.getRePassword())) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_INPUT_NOT_MATCHED);
        }

        if (!PasswordUtil.isPassword(joinDto.getPassword())) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT);
        }

        member.setName(joinDto.getName());
        Long memberId = memberRepository.saveMember(member);

        member = memberRepository.find(memberId);
        Password password = new Password();
        password.setMember(member);
        password.setMemberPassword(encoder.encode(joinDto.getPassword()));
        password.setExpireDate(LocalDateTime.now().plusMonths(6));
        memberRepository.savePassword(password);

        Role role = new Role();
        role.setMember(member);
        role.setRoleName("USER");
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
    public int deleteMember(Long memberId) {
        Member target = memberRepository.find(memberId);
        if (target == null) {
            throw new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND);
        }
        memberRepository.delete(target.getMemberId());
        return 1;
    }

    private void validateDuplicateEmail(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
            throw new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE);
        });
    }
}
