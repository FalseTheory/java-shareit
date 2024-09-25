package ru.practicum.shareit.item.model;

import lombok.Data;


@Data
public class Item {
    long id;
    String name;
    String description;
    long ownerId;
    int timesRented;
    Boolean available;
    Long bookingId;
}
