package net.nonworkspace.demo.domain.dto.recruit;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import net.nonworkspace.demo.domain.Company;

@Schema(title = "RECRUIT_REQ_02 : 회사 DTO")
public record CompanyDto(
    @Schema(description = "회사 ID") Long companyId,
    @Schema(description = "회사 이름", example = "Undefined Co.") @NotEmpty(message = "회사 이름을 입력하세요.") String companyName,
    @Schema(description = "회사 설명") String description,
    @Schema(description = "회사 이메일", example = "aaa@ddd.rrr") @NotEmpty(message = "회사 이메일을 입력하세요.") @Email String contactEmail,
    @Schema(description = "회사 전화번호") String contactPhone
) {

    public CompanyDto(Company company) {
        this(
            company.getId(),
            company.getCompanyName(),
            company.getDescription(),
            company.getContactEmail(),
            company.getContactPhone()
        );
    }

    public static io.swagger.v3.oas.models.media.Schema getSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>().type("object")
            .title("RECRUIT_REQ_02 : 회사 DTO")
            .addProperty("companyId", new NumberSchema().description("회사 ID"))
            .addProperty("companyName",
                new StringSchema().description("회사 이름").example("Undefined Co."))
            .addProperty("description", new StringSchema().description("회사 설명"))
            .addProperty("contactEmail",
                new EmailSchema().description("회사 이메일").example("aaa@ddd.rrr"))
            .addProperty("contactPhone", new StringSchema().description("회사 전화번호"));
    }
}
