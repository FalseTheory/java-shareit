package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserBookerDto;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    Long id;
    ItemShortDto item;
    UserBookerDto booker;
    BookingStatus status;
    LocalDateTime start;
    LocalDateTime end;
}
