package net.nonworkspace.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.JoinRequestDto;
import net.nonworkspace.demo.domain.dto.LoginRequestDto;
import net.nonworkspace.demo.service.AuthenticationService;
import net.nonworkspace.demo.service.MemberJpaService;
import net.nonworkspace.demo.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Value("${jwt.expiration_time}")
    private int expireTime;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        Long memberId = memberJpaService.join(joinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(memberId.toString());
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = authenticationService.getLoginToken(loginRequestDto);
        CookieUtil.addCookie(response, "auth_req", token, expireTime);
        return ResponseEntity.status(HttpStatus.OK).body("signed OK!");
    }
}
