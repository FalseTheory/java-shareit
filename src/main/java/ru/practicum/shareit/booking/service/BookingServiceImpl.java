package ru.practicum.shareit.booking.service;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingDto create(BookingCreateDto bookingCreateDto) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмета с id - " + bookingCreateDto.getItemId() + " не найдено"));
        User booker = userRepository.findById(bookingCreateDto.getBookerId())
                .orElseThrow(() -> new NotFoundException("Пользователя с id - " + bookingCreateDto.getBookerId() + " не найдено"));
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет c id " + item.getId() + " не доступен для бронирования.");
        }
        Booking booking = mapper.mapCreateDtoToBooking(bookingCreateDto);

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        booking = bookingRepository.save(booking);

        return mapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto processBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Букинг с id - " + bookingId + " не найден"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Пользователь с id - " + userId + " не имеет доступа к этому букингу");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return mapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Букинг с id - " + bookingId + " не найден"));
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new ForbiddenException("Пользователь с id - " + userId + " не имеет доступа к этому букингу");
        }
        return mapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerBookings(Long userId, BookingStatus state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id - " + userId + " не найдено"));

        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case REJECTED, WAITING, APPROVED, CANCELED -> bookingRepository.findOwnerBookings(userId, state).stream()
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case PAST -> bookingRepository.findOwnerBookings(userId).stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case FUTURE -> bookingRepository.findOwnerBookings(userId).stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case CURRENT -> bookingRepository.findOwnerBookings(userId).stream()
                    .filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            default -> bookingRepository.findOwnerBookings(userId).stream()
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
        };


    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(Long userId, BookingStatus state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id - " + userId + " не найдено"));

        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case REJECTED, WAITING, APPROVED, CANCELED -> bookingRepository.findUserBookings(userId, state).stream()
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case PAST -> bookingRepository.findUserBookings(userId).stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case FUTURE -> bookingRepository.findUserBookings(userId).stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            case CURRENT -> bookingRepository.findUserBookings(userId).stream()
                    .filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now))
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
            default -> bookingRepository.findUserBookings(userId).stream()
                    .map(mapper::mapToBookingDto)
                    .collect(Collectors.toList());
        };


    }
}
