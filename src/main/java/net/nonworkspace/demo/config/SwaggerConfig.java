package net.nonworkspace.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.List;
import net.nonworkspace.demo.domain.dto.batch.BatchJobExecutionDto;
import net.nonworkspace.demo.domain.dto.common.CommonResponseDto;
import net.nonworkspace.demo.domain.dto.member.MemberDto;
import net.nonworkspace.demo.domain.dto.member.MemberViewDto;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String AUTH_TOKEN_HEADER = "Authorization";

    private SecurityScheme securityScheme = new SecurityScheme()
        .type(Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(In.HEADER)
        .name(AUTH_TOKEN_HEADER);

    @Bean
    public GroupedOpenApi anonymousAPI() {
        return GroupedOpenApi.builder().group("01. anonymous")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("anonymous API")
                    .description("아무나 다 쓸 수 있는 API").version("1.0.0"))
            )
            .pathsToMatch(
                new String[]{"/api/recruit", "/api/recruit/**", "/api/auth/signup", "/api/auth/signin", "/api/board", "/api/board/**"})
            .build();
    }

    @Bean
    public GroupedOpenApi userRoleAPI() {
        return GroupedOpenApi.builder().group("02. user-role")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("user API")
                        .description("유저 권한 있어야 쓸 수 있는 API").version("1.0.0"))
                    .security(List.of(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)))
                    .components(
                        new Components()
                            .addSchemas("CommonResponseDto", new Schema<CommonResponseDto>())
                            .addSchemas("UserInfoDto", new Schema<UserInfoDto>())
                            .addSecuritySchemes(AUTH_TOKEN_HEADER, securityScheme))
            )
            .pathsToMatch(new String[]{"/user/**", "/api/auth/signout"})
            .build();
    }

    @Bean
    public GroupedOpenApi adminRoleAPI() {
        return GroupedOpenApi.builder().group("03. admin-role")
            .addOpenApiCustomizer(
                (c) -> c.info(new Info().title("admin API")
                        .description("관리자 권한 있어야 쓸 수 있는 API").version("1.0.0"))
                    .security(List.of(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)))
                    .components(
                        new Components()
                            // TODO : How to convert Dto record type to Schema<T> automatically?
                            .addSchemas("CommonResponseDto", new Schema<CommonResponseDto>())
                            .addSchemas("BatchJobExecutionDto", new Schema<BatchJobExecutionDto>())
                            .addSchemas("MemberDto", new Schema<>().type("object")
                                .addProperty("memberId", new NumberSchema())
                                .addProperty("name", new StringSchema())
                                .addProperty("email", new EmailSchema())
                                .addProperty("createDate", new DateTimeSchema()))
                            .addSchemas("MemberViewDto", new Schema<MemberViewDto>())
                            .addSchemas("RecruitDto", new Schema<RecruitDto>())
                            .addSchemas("RecruitViewDto", new Schema<RecruitViewDto>())
                            .addSchemas("CompanyDto", new Schema<CompanyDto>())
                            .addSecuritySchemes(AUTH_TOKEN_HEADER, securityScheme))
            )
            .pathsToMatch(new String[]{"/admin/**", "/batch", "/batch/**"})
            .build();
    }
}