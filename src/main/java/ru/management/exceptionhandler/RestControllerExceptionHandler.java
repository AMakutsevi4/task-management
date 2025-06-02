package ru.management.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.management.entity.exception.TagExistException;
import ru.management.entity.exception.ValidationErrorResponse;
import ru.management.entity.exception.Violation;

import java.util.List;

/**
 * Обработчик исключений
 */
@ControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {
    /**
     * Обрабатывает MethodArgumentNotValidException
     *
     * @param e - проброшенное исключение
     * @return - статус 400 и сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ValidationErrorResponse(violations);
    }

    /**
     * Обрабатывает TagExistException
     *
     * @param e - проброшенное исключение
     * @return - статус 500 и сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(TagExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onTagExistException(TagExistException e) {
        return new ValidationErrorResponse(List.of(new Violation("tagNameError", e.getMessage())));
    }

    /**
     * Обрабатывает EntityNotFoundException
     *
     * @param e - проброшенное исключение
     * @return - статус 500 и сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onEntityNotFoundException(EntityNotFoundException e) {
        return new ValidationErrorResponse(List.of(new Violation("entityError", e.getMessage())));
    }
}