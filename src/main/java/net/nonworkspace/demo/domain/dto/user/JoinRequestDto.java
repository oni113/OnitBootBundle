package net.nonworkspace.demo.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.utils.PasswordUtil;
import net.nonworkspace.demo.utils.StringUtil;

@Schema(title = "AUTH_REQ_01 : 회원 가입 요청 DTO")
public record JoinRequestDto(
    @NotEmpty(message = "이름을 입력해주세요.") @Schema(example = "John") String name,
    @NotNull(message = "이메일을 입력해주세요.") @Email(message = "잘못된 이메일 값 형식") @Schema(example = "aaa@adad.ee.rr") String email,
    @NotNull(message = "패스워드를 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]{8,}$",
        message = "패스워드 값을 영문 대문자, 영문 소문자, 숫자, 특수문자 조합 8자리 이상으로 입력해주세요."
    )
    @Schema(example = "Abb123@!")
    String password,
    @NotNull(message = "패스워드를 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]{8,}$",
        message = "패스워드 값을 영문 대문자, 영문 소문자, 숫자, 특수문자 조합 8자리 이상으로 입력해주세요."
    )
    @Schema(example = "Abb123@!")
    String rePassword
) {

    public JoinRequestDto(String name, String email, String password, String rePassword) {
        this.name = name;
        this.email = email;
        /*
        if (!password.equals(rePassword)) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_INPUT_NOT_MATCHED);
        }
         */
        this.password = password;
        this.rePassword = rePassword;
    }
}
