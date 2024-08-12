package net.nonworkspace.demo.domain.dto.recruit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.Recruit;
import net.nonworkspace.demo.domain.RecruitType;
import net.nonworkspace.demo.domain.Salary;

@Schema(title = "RECRUIT_REQ_02 : 리크루트 등록/수정 DTO")
public record RecruitViewDto(
    Long recruitId,
    @NotNull(message = "타입을 선택하세요.") @Schema(example = "REMOTE") RecruitType type,
    @NotEmpty(message = "제목을 입력하세요.") @Schema(example = "New Recruit for Typescript developer") String title,
    @Schema(example = "New Recruit for Typescript developer") String description,
    @NotNull(message = "연봉을 선택하세요.") @Schema(example = "OVER_$200K") Salary salary,
    @NotEmpty(message = "위치를 입력하세요.") String location,
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
