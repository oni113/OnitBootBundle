package net.nonworkspace.demo.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(title = "AUTH_REQ_02 : 로그인 요청 DTO")
public record LoginRequestDto(
    @NotNull(message = "이메일을 입력해주세요.") @Email String email,
    @NotNull(message = "패스워드를 입력해주세요.") String password
) {
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
