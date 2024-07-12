package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.UserInfoDto;
import net.nonworkspace.demo.service.AuthenticationService;
import net.nonworkspace.demo.service.MemberJpaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    private final MemberJpaService memberJpaService;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @GetMapping("/")
    public ResponseEntity<String> userIndex(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization", required = false) String token) throws Exception {
        return ResponseEntity.ok("유저용 API 접근 성공!");
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserInfoDto> whoAmI(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization", required = false) String token) {
        UserInfoDto userInfoDto = authenticationService.getUserDetailFromToken(token.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
    }
}
