package net.nonworkspace.demo.domain.dto.member;

import net.nonworkspace.demo.domain.Role;

public record RoleDto(
    Long id,
    String roleName
) {

    public RoleDto(Role role) {
        this(
            role.getId(),
            role.getRoleName()
        );
    }
}
