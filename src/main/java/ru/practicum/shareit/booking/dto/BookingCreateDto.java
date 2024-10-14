package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    @Positive
    @NotNull
    Long itemId;
    Long bookerId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    @Future
    LocalDateTime end;

    @AssertTrue
    public boolean isValidBookPeriod() {
        return end.isAfter(start);
    }

}
