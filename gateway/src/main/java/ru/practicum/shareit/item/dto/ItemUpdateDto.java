package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {
    Long id;
    Long ownerId;
    String name;
    String description;
    Boolean available;
}
