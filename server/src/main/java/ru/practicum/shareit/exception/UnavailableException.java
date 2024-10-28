package ru.practicum.shareit.exception;

public class UnavailableException extends RuntimeException {

    public UnavailableException(final String message) {
        super(message);
    }
}
