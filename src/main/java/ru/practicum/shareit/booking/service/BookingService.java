package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingCreateDto bookingCreateDto);

    BookingDto processBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getOwnerBookings(Long userId, BookingState state);

    List<BookingDto> getUserBookings(Long userId, BookingState state);
}
