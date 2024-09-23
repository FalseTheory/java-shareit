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
    Long ownerId;
    int timesRented;
    Boolean available;
    Long bookingId;
}
