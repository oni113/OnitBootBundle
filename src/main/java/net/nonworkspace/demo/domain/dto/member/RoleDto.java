package net.nonworkspace.demo.domain.dto.member;

import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import net.nonworkspace.demo.domain.entity.Role;

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

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .addProperty("id", new NumberSchema().description("권한 ID"))
            .addProperty("roleName", new StringSchema().description("권한 이름"));
    }
}
