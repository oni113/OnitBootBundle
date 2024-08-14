package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "USER API", description = "사용자 기능 API")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

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
    public ResponseEntity<UserInfoDto> myPage(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization") String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(((DemoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal()).userInfoDto());
        } catch (Exception e) {
            log.debug(ExceptionUtils.getMessage(e));
            throw e;
        }
    }
}
