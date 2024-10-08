package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;


@Data
public class Item {
    long id;
    String name;
    String description;
    User owner;
    Boolean available;
}
