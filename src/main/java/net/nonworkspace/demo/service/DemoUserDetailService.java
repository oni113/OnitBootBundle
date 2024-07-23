package net.nonworkspace.demo.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.dto.UserInfoDto;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DemoUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                    CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage()));
            return new DemoUserDetails(new UserInfoDto(member));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(
                CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage());
        } catch (Exception e) {
            log.debug(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    public UserDetails loadUserByUserId(Long memberId) throws UsernameNotFoundException {
        try {
            Member member = Optional.ofNullable(memberRepository.find(memberId))
                .orElseThrow(() -> new UsernameNotFoundException(
                    CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage()));
            return new DemoUserDetails(new UserInfoDto(member));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(
                CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage());
        } catch (Exception e) {
            log.debug(ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }
}
