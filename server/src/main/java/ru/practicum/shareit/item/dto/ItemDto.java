package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;


@Data
@AllArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentDto> comments;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;

    public record BookingShortDto(Long id, Long bookerId) {
    }
}
