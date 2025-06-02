package ru.management.entity.exception;

/**
 * Обработка ошибок тегов
 */
public class TagExistException extends RuntimeException {
    public TagExistException(String message) {
        super(message);
    }
}
