package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserBookerDto;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor @AllArgsConstructor
public class BookingDto {
    Long id;
    ItemShortDto item;
    UserBookerDto booker;
    BookingStatus status;
    LocalDateTime start;
    LocalDateTime end;
}
