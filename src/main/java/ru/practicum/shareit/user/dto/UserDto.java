package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    @NotNull
    String name;
    @Email
    @NotNull
    String email;
}
