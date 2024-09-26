package ru.practicum.shareit.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto notFoundException(final NotFoundException e) {
        log.info("Not Found {}", e.getMessage());
        return new ErrorResponseDto("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto conflictException(final ConflictException e) {
        log.info("Conflicting data {}", e.getMessage());
        return new ErrorResponseDto("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto validationException(final ValidationException e) {
        log.info("Validation Exception {}", e.getMessage());
        return new ErrorResponseDto("validation error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnexpectedException(final Exception e) {
        log.warn("Непредвиденная ошибка", e);
        return new ErrorResponseDto("Unexpected error", e.getMessage());
    }


}
