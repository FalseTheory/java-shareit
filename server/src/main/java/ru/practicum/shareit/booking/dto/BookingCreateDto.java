package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {

    Long itemId;
    Long bookerId;
    LocalDateTime start;

    LocalDateTime end;



}
