package net.nonworkspace.demo.domain.dto.user;

import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Role;
import net.nonworkspace.demo.domain.dto.member.RoleDto;

@Slf4j
public record UserInfoDto(
    Long userId,
    String email,
    String name,
    String password,
    List<Role> roles,
    boolean isAccountNonExpired,
    boolean isAccountNonLocked,
    boolean isCredentialsNonExpired,
    boolean isEnabled,
    boolean hasAdminRole
) {

    public UserInfoDto(Member member) {
        this(
            member.getMemberId(),
            member.getEmail(),
            member.getName(),
            member.getPasswords().stream()
                .filter(p -> p.getExpireDate().isAfter(LocalDateTime.now())).findAny().get()
                .getMemberPassword(),
            member.getRoles(),
            true,
            true,
            true,
            true,
            member.getRoles().stream().filter(r -> r.getRoleName().equals("ADMIN")).findAny()
                .isPresent()
        );
    }

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("AUTH_REQ_03 : 로그인 사용자 정보 DTO")
            .addProperty("userId", new NumberSchema().description("회원 ID"))
            .addProperty("email", new EmailSchema().description("회원 이메일"))
            .addProperty("name", new StringSchema().description("회원 이름"))
            .addProperty("password", new StringSchema().description("패스워드"))
            .addProperty("roles", RoleDto.getSchema().description("권한"))
            .addProperty("isAccountNonExpired", new BooleanSchema().description("계정 만료 여부 (true:유효/false:만료)"))
            .addProperty("isAccountNonLocked", new BooleanSchema().description("계정 잠김 여부 (true:활성/false:잠김)"))
            .addProperty("isCredentialsNonExpired", new BooleanSchema().description("권한 만료 여부 (true:유효/false:만료)"))
            .addProperty("isEnabled", new BooleanSchema().description("사용 여부"))
            .addProperty("hasAdminRole", new BooleanSchema().description("관리자 권한 여부"));
    }
}
