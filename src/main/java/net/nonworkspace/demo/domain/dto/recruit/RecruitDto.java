package net.nonworkspace.demo.domain.dto.recruit;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.NotNull;
import net.nonworkspace.demo.domain.entity.Recruit;
import net.nonworkspace.demo.domain.code.RecruitType;
import net.nonworkspace.demo.domain.code.Salary;

@Schema(title = "RECRUIT_REQ_01 : 리크루트 DTO")
public record RecruitDto(
        @Schema(description = "리크루트 ID") Long recruitId,
        @Schema(description = "리크루트 타입") @NotNull RecruitType type,
        @Schema(description = "리크루트 제목") @NotNull String title,
        @Schema(description = "리크루트 설명") String description,
        @Schema(description = "연봉") @NotNull Salary salary,
        @Schema(description = "위치") @NotNull String location,
        @Schema(description = "회사") @NotNull CompanyDto company
) {

    public RecruitDto(Recruit recruit) {
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
        return new io.swagger.v3.oas.models.media.Schema().type("object")
                .title("RECRUIT_REQ_01 : 리크루트 DTO")
                .addProperty("recruitId", new NumberSchema().description("리크루트 ID"))
                .addProperty("type",
                        new io.swagger.v3.oas.models.media.Schema<RecruitType>().description("리크루트 타입"))
                .addProperty("title", new StringSchema().description("리크루트 제목"))
                .addProperty("description", new StringSchema().description("리크루트 설명"))
                .addProperty("salary",
                        new io.swagger.v3.oas.models.media.Schema<Salary>().description("연봉"))
                .addProperty("location", new StringSchema().description("위치"));
    }
}
