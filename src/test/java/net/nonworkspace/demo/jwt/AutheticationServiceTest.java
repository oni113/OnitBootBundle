package net.nonworkspace.demo.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.LoginRequestDto;
import net.nonworkspace.demo.service.AuthenticationService;

@SpringBootTest
@Transactional
@Slf4j
class AutheticationServiceTest {

    @Autowired
    private AuthenticationService service;
    
    @Test
    @DisplayName("정상 아이디/패스워드 입력 후 토큰 값 발급되면 성공")
    void 로그인_인증_성공_토큰_생성() {
        // given
        LoginRequestDto dto = new LoginRequestDto(
            "jwt222@jjjjj.ww.cc",
            "Rkskekfk1@"
        );
        
        // when
        String token = service.getLoginToken(dto);
        log.debug("created token: {}", token);
        
        // then
        assertThat(!token.isEmpty());
    }
}
