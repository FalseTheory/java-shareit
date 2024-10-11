package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    @PostMapping
    public BookingDto bookItem() {
        return null;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking() {
        return null;
    }
    @GetMapping
    public List<BookingDto> getUsersBookings(@RequestParam BookingStatus status) {
        return null;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto processBooking(@RequestParam boolean approved) {
        return null;
    }
}
