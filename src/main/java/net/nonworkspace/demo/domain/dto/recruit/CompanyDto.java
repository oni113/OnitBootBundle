package net.nonworkspace.demo.domain.dto.recruit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.Company;

@Schema(title = "RECRUIT_REQ_02 : 회사 DTO")
public record CompanyDto(
    Long companyId,
    @NotEmpty(message = "회사 이름을 입력하세요.") @Schema(example = "Undefined Co.") String companyName,
    String description,
    @NotEmpty(message = "회사 이메일을 입력하세요.") @Schema(example = "aaa@ddd.rrr") @Email String contactEmail,
    String contactPhone
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
}
