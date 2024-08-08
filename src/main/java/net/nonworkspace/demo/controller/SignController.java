package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.common.CommonResponseDto;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.domain.dto.user.LoginRequestDto;
import net.nonworkspace.demo.service.AuthenticationService;
import net.nonworkspace.demo.service.MemberJpaService;
import net.nonworkspace.demo.utils.CookieUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Slf4j
public class SignController {

    private final AuthenticationService authenticationService;

    private final MemberJpaService memberJpaService;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @Value("${custom.jwt.cookie}")
    private String tokenCookieName;

    @Value("${jwt.expiration_time}")
    private int expireTime;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signUp(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        try {
            Long memberId = memberJpaService.join(joinRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto(
                memberId,
                "회원가입 성공"
            ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new CommonResponseDto(
                    -1L,
                    e.getMessage()
            ));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<CommonResponseDto> signIn(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            String token = authenticationService.getLoginToken(loginRequestDto);
            CookieUtil.addCookie(response, tokenCookieName, token, expireTime);
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto(
                1L,
                "로그인 성공"
            ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponseDto(
                -1L,
                "로그인 실패"
            ));
        }
    }

    @DeleteMapping("/signout")
    public ResponseEntity<CommonResponseDto> signOut(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization") String token) {
        try {
            CookieUtil.deleteCookie(request, response, tokenCookieName);
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto(
                1L,
                "로그아웃 성공"
            ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponseDto(
                -1L,
                "로그아웃 실패"
            ));
        }
    }
}
