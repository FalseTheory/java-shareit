package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserBookerDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private BookingCreateDto createDto = new BookingCreateDto(
            1L,
            1L,
            null,
            null
    );
    long userId = 1L;
    private BookingDto booking = new BookingDto(
            1L,
            new ItemShortDto(1L, "item"),
            new UserBookerDto(1L, "user"),
            BookingStatus.WAITING,
            null,
            null
    );

    @Test
    @DisplayName("Эндпоинт POST /bookings должен корректно работать")
    void bookItemTest() throws Exception {
        when(bookingService.create(any()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.booker").value(Matchers.aMapWithSize(2)))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()))
                .andExpect(jsonPath("$.start").doesNotExist())
                .andExpect(jsonPath("$.end").doesNotExist())
                .andExpect(jsonPath("$.item").value(Matchers.aMapWithSize(2)));

        verify(bookingService, times(1)).create(any());
    }

    @Test
    @DisplayName("Эндпоинт GET /bookings/{id} должен корректно работать")
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(1L, 1L))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.booker").value(Matchers.aMapWithSize(2)))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()))
                .andExpect(jsonPath("$.start").doesNotExist())
                .andExpect(jsonPath("$.end").doesNotExist())
                .andExpect(jsonPath("$.item").value(Matchers.aMapWithSize(2)));

        verify(bookingService, times(1)).getBooking(1L, 1L);
    }

    @Test
    @DisplayName("Эндпоинт GET /bookings/owner должен корректно работать")
    void getOwnerBookingsTest() throws Exception {
        when(bookingService.getOwnerBookings(1L, BookingState.ALL))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getOwnerBookings(1L, BookingState.ALL);
    }

    @Test
    @DisplayName("Эндпоинт GET /bookings должен корректно работать")
    void getUsersBookingsTest() throws Exception {
        when(bookingService.getUserBookings(1L, BookingState.ALL))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getUserBookings(1L, BookingState.ALL);
    }

    @Test
    @DisplayName("Эндпоинт POST /bookings/{id}?approved должен корректно работать")
    void processBookingTest() throws Exception {
        when(bookingService.processBooking(1L, 1L, true))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.booker").value(Matchers.aMapWithSize(2)))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()))
                .andExpect(jsonPath("$.start").doesNotExist())
                .andExpect(jsonPath("$.end").doesNotExist())
                .andExpect(jsonPath("$.item").value(Matchers.aMapWithSize(2)));

        verify(bookingService, times(1)).processBooking(1L, 1L, true);

    }
}