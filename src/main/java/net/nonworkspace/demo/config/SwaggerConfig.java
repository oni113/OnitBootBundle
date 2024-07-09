package net.nonworkspace.demo.config;

import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER,
        scheme = "bearer", bearerFormat = "JWT", paramName = "Authorization")
@Configuration
public class SwaggerConfig {

    private static final String AUTH_TOKEN_HEADER = "Authorization";

    @Bean
    public GroupedOpenApi demoAPI() {
        return GroupedOpenApi.builder().group("api")
                .addOpenApiCustomizer((c) -> c.info(new Info().title("demo project API")
                        .description("description demo project : API 설명").version("1.0.0"))
                .security(List.of(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)))
                ).build();
    }
}
