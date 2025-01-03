package ru.practicum.shareit.exception;

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
    @ResponseStatus
    public ErrorResponseDto unavailableException(final UnavailableException e) {
        log.info("Bad Request {}", e.getMessage());
        return new ErrorResponseDto("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto conflictException(final ConflictException e) {
        log.info("Conflicting data {}", e.getMessage());
        return new ErrorResponseDto("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto forbiddenException(final ForbiddenException e) {
        log.info("Forbidden Exception {}", e.getMessage());
        return new ErrorResponseDto("forbidden exception", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnexpectedException(final Exception e) {
        log.warn("Непредвиденная ошибка", e);
        return new ErrorResponseDto("Unexpected error", e.getMessage());
    }


}
