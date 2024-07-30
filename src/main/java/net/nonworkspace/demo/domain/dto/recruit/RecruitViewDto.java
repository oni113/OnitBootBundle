package net.nonworkspace.demo.domain.dto.recruit;

import jakarta.validation.constraints.NotNull;
import java.util.Locale;
import net.nonworkspace.demo.domain.Recruit;
import net.nonworkspace.demo.domain.RecruitType;

public record RecruitViewDto(
    Long recruitId,
    @NotNull RecruitType type,
    @NotNull String title,
    String description,
    @NotNull String salary,
    @NotNull String location,
    CompanyDto company
    ) {
    public RecruitViewDto(Recruit recruit) {
        this(
            recruit.getId(),
            recruit.getType(),
            recruit.getTitle(),
            recruit.getDescription(),
            recruit.getSalary(),
            recruit.getLocation(),
            new CompanyDto(recruit.getCompany())
        );
    }
}
