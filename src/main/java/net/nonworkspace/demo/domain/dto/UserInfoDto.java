package net.nonworkspace.demo.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Role;

public record UserInfoDto(
    Long userId,
    String email,
    String name,
    String password,
    List<Role> roles,
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
            true,   // need for design via new business requirement
            true,   // need for design via new business requirement
            true,   // need for design via new business requirement
            true    // need for design via new business requirement
        );
    }
}
