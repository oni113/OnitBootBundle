package net.nonworkspace.demo.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import net.nonworkspace.demo.service.MemberJpaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.user.LoginRequestDto;
import net.nonworkspace.demo.service.AuthenticationService;

@SpringBootTest
@Transactional
@Slf4j
class AutheticationServiceTest {

    @Autowired
    private MemberJpaService memberJpaService;

    @Autowired
    private AuthenticationService authenticationService;
    
    @Test
    @DisplayName("정상 아이디/패스워드 입력 후 토큰 값 발급되면 성공")
    void 로그인_인증_성공_토큰_생성() {
        // given
        JoinRequestDto joinDto = new JoinRequestDto(
            "새회원",
            "ttt@ttt.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        Long newMemberId = memberJpaService.join(joinDto);

        LoginRequestDto loginDto = new LoginRequestDto(
            "ttt@ttt.ttt",
            "Rkskekfk1!"
        );
        
        // when
        String token = authenticationService.getLoginToken(loginDto);
        log.info("created token: {}", token);
        
        // then
        assertThat(!token.isEmpty());
        UserInfoDto userInfoDto = authenticationService.getUserDetailFromToken(token);
        assertThat(userInfoDto.userId() == newMemberId);
        assertThat(userInfoDto.email().equals("ttt@ttt.ttt"));
        assertThat(userInfoDto.name().equals("새회원"));
    }
}
