package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private final long userId = 1L;

    private final ItemCreateDto newItem = new ItemCreateDto(
            "name",
            "desc",
            true,
            1L,
            1L
    );
    private final ItemDto item = new ItemDto(
            1L,
            "name",
            "desc",
            true,
            null,
            null,
            null
    );

    @Test
    @DisplayName("Эндпоинт POST /items должен корректно работать")
    void createItemTest() throws Exception {
        when(itemService.create(any()))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(newItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).create(any());
    }

    @Test
    @DisplayName("Эндпоинт GET /items/{id} должен корректно работать")
    void getItemByIdTest() throws Exception {
        when(itemService.getById(1L, 1L)).thenReturn(item);

        mvc.perform(get("/items/" + 1)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).getById(1L, 1L);
    }

    @Test
    @DisplayName("Эндпоинт PATCH /items/{id} должен корректно работать")
    void updateItemTest() throws Exception {
        when(itemService.update(any()))
                .thenReturn(item);

        mvc.perform(patch("/items/" + 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(newItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).update(any());
    }

    @Test
    @DisplayName("Эндпоинт GET /items должен корректно работать")
    void getItemsForUserTest() throws Exception {
        when(itemService.getItemsForUser(1L)).thenReturn(Collections.emptyList());

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).getItemsForUser(1L);
    }

    @Test
    @DisplayName("Эндпоинт GET /items/search должен корректно работать")
    void searchAvailableItemsTest() throws Exception {
        when(itemService.searchAvailableItems(1L, "text")).thenReturn(Collections.emptyList());

        mvc.perform(get("/items/search?text=text")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).searchAvailableItems(1L, "text");
    }

    @Test
    @DisplayName("Эндпоинт POST /items/{id}/comment должен корректно работать")
    void postCommentTest() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                "comment",
                1L,
                1L
        );
        CommentDto commentDto = new CommentDto(
                1L,
                "comment",
                null,
                "author"
        );

        when(itemService.postComment(commentCreateDto)).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.created").doesNotExist())
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));

        verify(itemService, times(1)).postComment(any());

    }
}