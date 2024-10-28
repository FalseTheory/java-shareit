package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceTest {
    private final EntityManager em;
    private final UserService service;
    private final UserMapper mapper;

    private final UserDto user1 = new UserDto(
            1L,
            "ivan",
            "ivan@mail.ru"
    );
    private final UserDto user2 = new UserDto(
            2L,
            "john",
            "john@mail.ru"
    );
    private final UserDto user3 = new UserDto(
            3L,
            "ira",
            "ira@mail.ru"
    );
    private final UserCreateDto newUser = new UserCreateDto(
            "vasya",
            "vasya@mail.ru"
    );
    private final UserDto user4Created = new UserDto(
            4L,
            "vasya",
            "vasya@mail.ru"
    );
    private final UserUpdateDto updateDto = new UserUpdateDto(
            3L,
            "danya",
            "danya@mail.ru"
    );
    private final UserUpdateDto updateDtoNulls = new UserUpdateDto(
            2L,
            null,
            null
    );

    private final List<UserDto> allUsers = List.of(user1, user2, user3);

    @Test
    @Rollback
    @DisplayName("Пользователь должен корректно создаваться")
    void createTest() {

        service.create(newUser);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.id=:id", User.class);
        query.setParameter("id", 4L);
        UserDto userDto = mapper.mapToUserDto(query.getSingleResult());

        Assertions.assertEquals(userDto, user4Created);


    }

    @Test
    @DisplayName("Пользователь должен корректно извлекаться по id")
    void getTest() {

        UserDto userDto = service.get(user1.getId());

        Assertions.assertEquals(userDto, user1);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.id=:id", User.class);
        query.setParameter("id", user1.getId());
        userDto = mapper.mapToUserDto(query.getSingleResult());

        Assertions.assertEquals(userDto, user1);

    }

    @Test
    @DisplayName("Список пользователей должен корректно возвращаться")
    void getAllTest() {

        List<UserDto> userDtoList = service.getAll();

        Assertions.assertEquals(userDtoList, allUsers);
    }

    @Test
    @Rollback
    @DisplayName("Функция обновления пользователя должна работать корректно")
    void updateTest() {
        UserDto userDto = service.update(updateDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.id=:id", User.class);
        query.setParameter("id", updateDto.getId());
        UserDto userDtoDb = mapper.mapToUserDto(query.getSingleResult());


        Assertions.assertEquals(userDto, userDtoDb);

        userDto = service.update(updateDtoNulls);
        query.setParameter("id", updateDtoNulls.getId());
        userDtoDb = mapper.mapToUserDto(query.getSingleResult());

        Assertions.assertEquals(userDto, userDtoDb);
    }

    @Test
    @DisplayName("При попытке найти несуществующего пользователя должна выбрасываться ошибка")
    void notFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(33L));
        Assertions.assertThrows(NotFoundException.class,
                () -> service.update(new UserUpdateDto(
                        33L,
                        "text",
                        "test@mail.ru"
                )));
    }

    @Test
    @DisplayName("Пользователь должен удаляться из базы")
    @Rollback
    void deleteTest() {
        service.delete(user3.getId());

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);

        assertEquals(query.getResultList().size(), allUsers.size() - 1);

    }
}