package net.nonworkspace.demo.domain.dto.recruit;

import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.Recruit;
import net.nonworkspace.demo.domain.RecruitType;

public record RecruitDto(
    Long recruitId,
    @NotNull Long companyId,
    @NotNull RecruitType type,
    @NotNull String title,
    String description,
    @NotNull String salary,
    @NotNull String location
) {
    public RecruitDto(Recruit recruit) {
        this(
            recruit.getId(),
            recruit.getCompany().getId(),
            recruit.getType(),
            recruit.getTitle(),
            recruit.getDescription(),
            recruit.getSalary(),
            recruit.getLocation()
        );
    }
}
