package net.nonworkspace.demo.service;

import java.time.LocalDateTime;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.dto.UserInfoDto;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage()));
            UserInfoDto userInfo = new UserInfoDto();
            userInfo.setUserId(member.getMemberId());
            userInfo.setEmail(member.getEmail());
            userInfo.setName(member.getName());
            userInfo.setPassword(member.getPasswords().stream()
                .filter(pw -> pw.getExpireDate().isAfter(LocalDateTime.now())).findAny().get()
                .getMemberPassword());

            return new DemoUserDetails(userInfo);
        } catch (Exception e) {
            throw new UsernameNotFoundException(
                    CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage());
        }
    }

    @Transactional
    public UserDetails loadUserByUserId(Long memberId) throws UsernameNotFoundException {
        try {
            Member member = memberRepository.find(memberId);
            if (member == null) {
                throw new UsernameNotFoundException(
                        CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage());
            }
            UserInfoDto userInfo = new UserInfoDto();
            userInfo.setUserId(member.getMemberId());
            userInfo.setEmail(member.getEmail());
            userInfo.setName(member.getName());
            userInfo.setPassword(member.getPasswords().stream()
                    .filter((p) -> p.getExpireDate().isAfter(LocalDateTime.now())).findAny().get()
                    .getMemberPassword());

            return new DemoUserDetails(userInfo);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(
                    CommonBizExceptionCode.NOT_EXIST_MEMBER.getMessage());
        }
    }
}
