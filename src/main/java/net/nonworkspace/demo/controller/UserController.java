package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import net.nonworkspace.demo.service.AuthenticationService;
import net.nonworkspace.demo.service.MemberJpaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> userIndex() throws Exception {
        return ResponseEntity.ok("유저용 API 접근 성공!");
    }

    @Operation(summary = "로그인 사용자 정보 조회", description = "로그인한 사용자 정보를 조회한다.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "로그인 사용자 정보 조회 성공",
                content = @Content(
                    schema = @Schema(implementation = UserInfoDto.class)
                )
            )
        }
    )
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoDto> myPage() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(((DemoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).userInfoDto());
    }
}
