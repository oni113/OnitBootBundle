package net.nonworkspace.demo.domain.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(title = "COM_RES_01 : 공통 응답 DTO")
public record CommonResponseDto(
    @Schema(description = "응답 결과 코드값") @NotNull Long result,
    @Schema(description = "응답 결과 메세지") @NotNull String message
) {

}
