package net.nonworkspace.demo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER,
    scheme = "bearer", bearerFormat = "JWT", paramName = "Authorization")
@Configuration
public class SwaggerConfig {

    private static final String AUTH_TOKEN_HEADER = "Authorization";

    @Bean
    public GroupedOpenApi anonymousAPI() {
        return GroupedOpenApi.builder().group("01. anonymous")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("anonymous API")
                    .description("아무나 다 쓸 수 있는 API").version("1.0.0"))
            )
            .pathsToMatch(new String[]{"/api/**", "/login", "/login/**"})
            .build();
    }

    @Bean
    public GroupedOpenApi userRoleAPI() {
        return GroupedOpenApi.builder().group("02. user-role")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("user API")
                        .description("유저 권한 있어야 쓸 수 있는 API").version("1.0.0"))
                    .security(List.of(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)))
            )
            .pathsToMatch(new String[]{"/user/**"})
            .build();
    }

    @Bean
    public GroupedOpenApi adminRoleAPI() {
        return GroupedOpenApi.builder().group("03. admin-role")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("admin API")
                        .description("관리자 권한 있어야 쓸 수 있는 API").version("1.0.0"))
                    .security(List.of(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)))
            )
            .pathsToMatch(new String[]{"/admin/**", "/batch", "/batch/**"})
            .build();
    }
}
