package net.nonworkspace.demo.domain.dto.common;

import jakarta.validation.constraints.NotNull;

public record CommonResponseDto(
    @NotNull Long result,
    @NotNull String message
) {

}
