package ru.management.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.management.dto.tag.TagResponse;
import ru.management.enums.TaskPriority;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskResponse(
        @Schema(description = "Идентификатор задачи", example = "1")
        Long id,

        @Schema(description = "Название задачи", example = "Закончить отчёт")
        String name,

        @Schema(description = "Описание задачи", example = "Подробное описание задачи")
        String description,

        @Schema(description = "Дата завершения задачи", example = "2025-07-01T10:41")
        LocalDateTime scheduledDate,

        @Schema(description = "Приоритет задачи", example = "IMPORTANT", defaultValue = "USUAL")
        TaskPriority priority,

        @Schema(description = "Теги задачи", example = "Отправить офер кандидату")
        Set<TagResponse> tags
) {
}