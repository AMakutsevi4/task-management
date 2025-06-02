package ru.management.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskPriorityResponse(
        @Schema(description = "Приоритет задачи", example = "IMPORTANT")
        String priority,
        @Schema(description = "Уровень приоритета", example = "5")
        int level
) {
}