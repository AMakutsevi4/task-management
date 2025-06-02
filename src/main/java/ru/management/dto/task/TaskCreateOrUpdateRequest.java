package ru.management.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.management.enums.TaskPriority;

import java.time.LocalDateTime;

public record TaskCreateOrUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 55, message = "Длина наименования задачи должна быть от 2 до 55 символов")
        @Schema(description = "Название задачи", example = "Закончить отчёт")
        String name,

        @Schema(description = "Описание задачи", example = "Подробное описание задачи")
        String description,

        @NotNull
        @FutureOrPresent(message = "Дата завершения не должна быть меньше текущей даты")
        @Schema(description = "Дата завершения задачи", example = "2025-07-01T10:41")
        LocalDateTime scheduledDate,

        @NotNull
        @Schema(description = "Приоритет задачи", example = "USUAL")
        TaskPriority priority
) {
}