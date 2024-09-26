package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    Long bookedUserId;
    LocalDateTime bookingStartTime;
    LocalDateTime bookingEndTime;
}
