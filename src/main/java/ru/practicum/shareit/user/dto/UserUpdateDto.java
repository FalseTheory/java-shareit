package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    Long id;
    String name;
    String email;
}
