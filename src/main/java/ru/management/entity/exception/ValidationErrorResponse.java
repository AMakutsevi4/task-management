package ru.management.entity.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Запрос на получение сообщения об ошибке валидации
 */
public record ValidationErrorResponse(

        @Schema(title = "Список ошибок валидации")
        @JsonProperty(value = "violationsErrors")
        List<Violation> violations) {
}