package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    Long id;
    String name;
    String description;
    int timesRented;
    boolean isBooked;
    Long bookingId;
}
