package ru.management.entity.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Тело с сообщением об ошибке в валидации
 */
public record Violation(
        @Schema(title = "Название поля")
        @JsonProperty(value = "fieldName")
        String fieldName,

        @JsonProperty(value = "message")
        @Schema(title = "Сообщение об ошибке валидации")
        String message) {
}