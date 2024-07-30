package net.nonworkspace.demo.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Role;

@Slf4j
@Schema(title = "AUTH_REQ_03 : 로그인 사용자 정보 DTO")
public record UserInfoDto(
    @Schema(description = "회원 ID") Long userId,
    @Schema(description = "이메일") String email,
    @Schema(description = "이름") String name,
    @Schema(description = "패스워드") String password,
    @Schema(description = "권한") List<Role> roles,
    boolean isAccountNonExpired,
    boolean isAccountNonLocked,
    boolean isCredentialsNonExpired,
    boolean isEnabled
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
            /*
            new ArrayList<>() {
                @Override
                public UserRoleDto get(int index) {
                    log.info("======= Convert Role to UserRoleDto roleName: {}",
                        member.getRoles().get(index).getRoleName());
                    return new UserRoleDto(
                        member.getRoles().get(index));
                }

                @Override
                public int size() {
                    return member.getRoles().size();
                }
            },

             */
            true,
            true,
            true,
            true
        );
    }
}
