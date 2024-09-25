package ru.practicum.shareit.user.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class UserMapper {

    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return userDto;
    }

    public User mapCreateDtoToUser(UserCreateDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());

        return user;
    }

    public User mapUpdateDtoToUser(UserUpdateDto userUpdateDto) {
        User user = new User();

        user.setId(userUpdateDto.getId());
        user.setEmail(userUpdateDto.getEmail());
        user.setName(userUpdateDto.getName());

        return user;
    }
}
