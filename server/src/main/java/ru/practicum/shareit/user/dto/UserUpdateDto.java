package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDto {
    Long id;
    String name;
    @Email
    String email;

    @AssertFalse(message = "Передаваемое имя не может быть пустым")
    public boolean isNotBlankIfNotNull() {
        if (name != null) {
            return name.isBlank();
        }
        return false;
    }
}
