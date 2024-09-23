package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validators.OnCreate;

@Data
public class UserDto {
    Long id;
    @NotNull(groups = OnCreate.class, message = "имя должно быть указано")
    String name;
    @Email(groups = {OnCreate.class}, message = "email должен быть корректным")
    @NotNull(groups = OnCreate.class, message = "email должен быть указан")
    String email;
}
