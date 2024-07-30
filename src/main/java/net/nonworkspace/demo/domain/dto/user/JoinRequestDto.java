package net.nonworkspace.demo.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.utils.PasswordUtil;
import net.nonworkspace.demo.utils.StringUtil;

@Schema(title = "AUTH_REQ_01 : 회원 가입 요청 DTO")
public record JoinRequestDto(
    @NotNull(message = "이름을 입력해주세요.") String name,
    @NotNull(message = "이메일을 입력해주세요.") @Email String email,
    @NotNull(message = "패스워드를 입력해주세요.") String password,
    @NotNull(message = "패스워드를 입력해주세요.") String rePassword
) {

    public JoinRequestDto(String name, String email, String password, String rePassword) {
        this.name = name;
        if (!StringUtil.isEmail(email)) {
            throw new CommonBizException(CommonBizExceptionCode.INVALID_EMAIL_FORMAT);
        }
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
