package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private UserDto newUser = new UserDto(
            1L,
            "Name",
            "Hello@mail.com"
    );
    private UserUpdateDto updateUser = new UserUpdateDto(
            1L,
            "Name",
            "Hello@mail.com"
    );

    @Test
    @DisplayName("Эндпоинт POST /users должен корректно работать")
    void createTest() throws Exception {
        when(userService.create(any()))
                .thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(newUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()));

        verify(userService, times(1)).create(any());
    }

    @Test
    @DisplayName("Эндпоинт GET /users/{id} должен корректно работать")
    void getUserByIdTest() throws Exception {
        when(userService.get(1L)).thenReturn(newUser);

        mockMvc.perform(get("/users/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()));

        verify(userService, times(1)).get(1L);

    }

    @Test
    @DisplayName("Эндпоинт GET /users должен корректно работать")
    void testGetAll() throws Exception {

        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Эндпоинт PATCH /users/{id} должен корректно работать")
    void updateTest() throws Exception {
        when(userService.update(any())).thenReturn(newUser);

        mockMvc.perform(patch("/users/" + 1L)
                        .content(mapper.writeValueAsString(updateUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()));

        verify(userService, times(1)).update(updateUser);
    }

    @Test
    @DisplayName("Эндпоинт DELETE /users/{id} должен корректно работать")
    void deleteTest() throws Exception {

        mockMvc.perform(delete("/users/" + 1L))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(1L);
    }
}