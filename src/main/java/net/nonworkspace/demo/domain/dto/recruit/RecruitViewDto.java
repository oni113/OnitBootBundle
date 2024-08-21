package net.nonworkspace.demo.domain.dto.recruit;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.entity.Recruit;
import net.nonworkspace.demo.domain.code.RecruitType;
import net.nonworkspace.demo.domain.code.Salary;

@Schema(title = "RECRUIT_REQ_02 : 리크루트 등록/수정 DTO")
public record RecruitViewDto(
    @Schema(description = "리크루트 ID") Long recruitId,
    @Schema(description = "리크루트 타입", example = "REMOTE") @NotNull(message = "타입을 선택하세요.") RecruitType type,
    @Schema(description = "리크루트 제목", example = "New Recruit for Typescript developer") @NotEmpty(message = "제목을 입력하세요.") String title,
    @Schema(description = "리크루트 설명", example = "Description New Recruit for Typescript developer") String description,
    @Schema(description = "연봉", example = "OVER_$200K") @NotNull(message = "연봉을 선택하세요.") Salary salary,
    @Schema(description = "위치") @NotEmpty(message = "위치를 입력하세요.") String location,
    @Schema(description = "회사") CompanyDto company
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

    public static io.swagger.v3.oas.models.media.Schema getSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>().type("object")
            .title("RECRUIT_REQ_02 : 리크루트 등록/수정 DTO")
            .addProperty("recruitId", new NumberSchema().description("리크루트 ID"))
            .addProperty("type",
                new io.swagger.v3.oas.models.media.Schema<RecruitType>().description("리크루트 타입")
                    .example("REMOTE"))
            .addProperty("title", new StringSchema().description("리크루트 제목")
                .example("New Recruit for Typescript developer"))
            .addProperty("description", new StringSchema().description("리크루트 설명")
                .example("Description New Recruit for Typescript developer"))
            .addProperty("salary",
                new io.swagger.v3.oas.models.media.Schema<Salary>().description("연봉")
                    .example("OVER_$200K"))
            .addProperty("location", new StringSchema().description("위치"))
            .addProperty("company", CompanyDto.getSchema());
    }
}
