package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreateDto {

    Long itemId;
    Long bookerId;
    LocalDateTime start;
    LocalDateTime end;


}
