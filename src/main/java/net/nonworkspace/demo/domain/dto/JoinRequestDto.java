package net.nonworkspace.demo.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.utils.PasswordUtil;

@Data
@NoArgsConstructor
@Schema(title = "AUTH_REQ_01 : 회원 가입 요청 DTO")
public class JoinRequestDto {

    @NotNull(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @NotNull(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "패스워드를 입력해주세요.")
    private String password;

    @NotNull(message = "패스워드를 입력해주세요.")
    private String rePassword;

    public JoinRequestDto(String name, String email, String password, String rePassword) {
        this.name = name;
        this.email = email;
        if (!PasswordUtil.isPassword(password)) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT);
        }
        if (!password.equals(rePassword)) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_INPUT_NOT_MATCHED);
        }
        this.password = password;
        this.rePassword = rePassword;
    }
}
