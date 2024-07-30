package net.nonworkspace.demo.domain.dto.user;

import net.nonworkspace.demo.domain.Role;

public record UserRoleDto(
    Long memberId,
    Long roleId,
    String roleName
) {

    public UserRoleDto(Role role) {
        this(
            role.getMember().getMemberId(),
            role.getId(),
            role.getRoleName()
        );
    }
}
