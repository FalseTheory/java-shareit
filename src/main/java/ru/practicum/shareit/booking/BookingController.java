package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody @Valid BookingCreateDto bookingCreateDto) {

        log.info("creating booking - {}", bookingCreateDto);
        bookingCreateDto.setBookerId(userId);
        BookingDto bookingDto = bookingService.create(bookingCreateDto);
        log.info("booking created successfully");
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable @Positive Long bookingId) {
        log.info("getting booking with id - {}, for user - {}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("getting owner bookings for user - {} with state - {}", userId, state);
        return bookingService.getOwnerBookings(userId, state);
    }

    @GetMapping
    public List<BookingDto> getUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("getting all bookings for user - {} with state - {}", userId, state);
        return bookingService.getUserBookings(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto processBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable @Positive Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("processing booking with id - {} by user - {}, approved - {}", bookingId, userId, approved);
        BookingDto bookingDto = bookingService.processBooking(userId, bookingId, approved);
        log.info("processing went successfully");
        return bookingDto;
    }
}
