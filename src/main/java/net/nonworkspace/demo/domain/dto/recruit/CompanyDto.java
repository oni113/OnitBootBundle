package net.nonworkspace.demo.domain.dto.recruit;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.Company;

public record CompanyDto(
    Long companyId,
    @NotNull String companyName,
    String description,
    @NotNull @Email String contactEmail,
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
