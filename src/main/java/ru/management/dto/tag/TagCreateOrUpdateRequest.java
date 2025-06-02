package ru.management.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record TagCreateOrUpdateRequest(
        @Schema(description = "Название тега", example = "backend")
        @Size(min = 2, max = 50, message = "Длина тега должна быть от 2 до 50 символов")
        String name
) {
}