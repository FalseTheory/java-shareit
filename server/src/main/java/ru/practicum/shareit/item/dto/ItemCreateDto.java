package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ItemCreateDto {
    String name;
    String description;
    Boolean available;
    Long ownerId;
    Long requestId;
}
