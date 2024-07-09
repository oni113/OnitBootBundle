package net.nonworkspace.demo.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "AUTH_REQ_02 : 로그인 요청 DTO")
public class LoginRequestDto {

    @NotNull(message = "이메일을 입력해주세요.")
    @Email
    private String email;
    
    @NotNull(message = "패스워드를 입력해주세요.")
    private String password;
}
