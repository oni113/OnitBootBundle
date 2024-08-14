package net.nonworkspace.demo.domain.dto.common;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.NotNull;

@Schema(title = "COM_RES_01 : 공통 응답 DTO")
public record CommonResponseDto(
    @NotNull @Schema(description = "응답 결과 코드값") Long result,
    @NotNull @Schema(description = "응답 결과 메세지") String message
) {

    public static io.swagger.v3.oas.models.media.Schema getSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>().type("object")
            .name("CommonResponseDto")
            .title("COM_RES_01 : 공통 응답 DTO")
            .addProperty("result", new NumberSchema().description("응답 결과 코드값"))
            .addProperty("message", new StringSchema().description("응답 결과 메세지"));
    }
}
