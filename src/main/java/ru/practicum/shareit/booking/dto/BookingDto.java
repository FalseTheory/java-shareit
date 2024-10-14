package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    Long id;
    Item item;
    User booker;
    BookingStatus status;
    LocalDateTime start;
    LocalDateTime end;
}
