package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody @Valid BookingCreateDto bookingCreateDto) {


        return bookingClient.bookItem(userId, bookingCreateDto);

    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable @Positive Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("getting owner bookings for user - {} with state - {}", userId, state);
        return bookingClient.getOwnerBookings(userId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingClient.getUsersBookings(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> processBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable @Positive Long bookingId,
                                                 @RequestParam boolean approved) {

        return bookingClient.processBooking(userId, bookingId, approved);
    }
}
