package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemDto mapToItemDto(Item item);

    @Mapping(target = "comments", source = "commentDtoList")
    @Mapping(target = "lastBooking", expression = "java(getLastBooking(bookings))")
    @Mapping(target = "nextBooking", expression = "java(getNextBooking(bookings))")
    ItemDto mapToItemDto(Item item, List<Booking> bookings, List<CommentDto> commentDtoList);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item mapCreateDtoToItem(ItemCreateDto itemDto);


    default ItemDto.BookingShortDto getNextBooking(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        Booking nextBooking = null;
        for (Booking booking : bookings) {
            if (booking.getStart().isAfter(now)) {
                if (nextBooking == null || !nextBooking.getStart().isAfter(booking.getStart())) {
                    nextBooking = booking;
                }
            }
        }
        if (nextBooking == null) {
            return null;
        }
        return new ItemDto.BookingShortDto(nextBooking.getId(), nextBooking.getBooker().getId());
    }

    default ItemDto.BookingShortDto getLastBooking(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = null;
        for (Booking booking : bookings) {
            if (booking.getEnd().isBefore(now)
                    || (booking.getStart().isBefore(now) && booking.getEnd().isAfter(now))) {
                if (lastBooking == null || !lastBooking.getEnd().isBefore(booking.getEnd())) {
                    lastBooking = booking;
                }
            }

        }
        if (lastBooking == null) {
            return null;
        }
        return new ItemDto.BookingShortDto(lastBooking.getId(), lastBooking.getBooker().getId());
    }
}
