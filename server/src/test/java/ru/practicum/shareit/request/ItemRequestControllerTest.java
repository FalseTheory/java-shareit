package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService service;

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private long userId = 1L;

    ItemRequestCreateDto newRequest = new ItemRequestCreateDto("new");

    ItemRequestDto request = new ItemRequestDto(1L, 1L,
            "new", null, null);


    @Test
    @DisplayName("Эндпоинт POST /requests должен корректно работать")
    void addRequest() throws Exception {
        when(service.add(anyLong(), any()))
                .thenReturn(request);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(newRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.owner_id").value(request.getOwnerId()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").doesNotExist())
                .andExpect(jsonPath("$.items").doesNotExist());

        verify(service, times(1)).add(anyLong(), any());

    }

    @Test
    @DisplayName("Эндпоинт GET /requests должен корректно работать")
    void getAllUserRequest() throws Exception {
        when(service.getAllUserRequest(1L))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllUserRequest(1L);
    }

    @Test
    @DisplayName("Эндпоинт GET /requests/all должен корректно работать")
    void getAllRequests() throws Exception {

        when(service.getAllRequests())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllRequests();
    }

    @Test
    @DisplayName("Эндпоинт GET /requests/{id} должен корректно работать")
    void getRequest() throws Exception {
        when(service.get(1L))
                .thenReturn(request);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.owner_id").value(request.getOwnerId()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").doesNotExist())
                .andExpect(jsonPath("$.items").doesNotExist());

        verify(service, times(1)).get(1L);
    }
}