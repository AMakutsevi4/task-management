package ru.management.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagResponse(
        @Schema(description = "ID тега", example = "1")
        Long id,

        @Schema(description = "Название тега", example = "backend")
        String name
) {
}