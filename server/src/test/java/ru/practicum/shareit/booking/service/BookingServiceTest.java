package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserBookerDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BookingServiceTest {
    private final EntityManager em;
    private final BookingService service;
    private final BookingMapper mapper;

    private final long bookerId = 3L;
    private final long item1OwnerId = 1L;

    private final BookingDto booking1 = new BookingDto(
            1L,
            new ItemShortDto(2L, "item2"),
            new UserBookerDto(3L, "ira"),
            BookingStatus.APPROVED,
            LocalDateTime.of(2011, 1, 1, 0, 0, 0),
            LocalDateTime.of(2011, 2, 1, 0, 0, 0)
    );
    private final BookingDto booking2 = new BookingDto(
            2L,
            new ItemShortDto(1L, "item1"),
            new UserBookerDto(3L, "ira"),
            BookingStatus.APPROVED,
            LocalDateTime.of(2012, 1, 1, 0, 0, 0),
            LocalDateTime.of(2013, 2, 1, 0, 0, 0)
    );
    private final BookingCreateDto createDto = new BookingCreateDto(
            1L,
            3L,
            LocalDateTime.of(2025, 1, 1, 0, 0, 0),
            LocalDateTime.of(2025, 2, 1, 0, 0, 0)

    );
    private final BookingCreateDto createCurrentDto = new BookingCreateDto(
            1L,
            3L,
            LocalDateTime.of(2024, 10, 28, 0, 0, 0),
            LocalDateTime.of(2024, 11, 1, 0, 0, 0)

    );
    private final List<BookingDto> allBookings = List.of(booking1, booking2);

    @Test
    @DisplayName("Пользователь без доступа не должен получать и уметь обрабатывать букинг")
    void forbiddenTest() {
        Assertions.assertThrows(ForbiddenException.class,
                () -> service.getBooking(1L, 1L));
        Assertions.assertThrows(ForbiddenException.class,
                () -> service.processBooking(3L, 1L, true));
    }


    @Test
    @Rollback
    @DisplayName("Тесты ошибок NotFound и Unavailable")
    void unavailableAndNotFoundTest() {


        createDto.setItemId(2L);
        Assertions.assertThrows(UnavailableException.class,
                () -> service.create(createDto));

        createDto.setItemId(1L);

        BookingCreateDto fakeDto = new BookingCreateDto(
                15L,
                1L,
                LocalDateTime.of(2024, 10, 28, 0, 0, 0),
                LocalDateTime.of(2024, 11, 1, 0, 0, 0)
        );

        Assertions.assertThrows(NotFoundException.class,
                () -> service.create(fakeDto));

        fakeDto.setItemId(1L);
        fakeDto.setBookerId(33L);

        Assertions.assertThrows(NotFoundException.class,
                () -> service.create(fakeDto));

        Assertions.assertThrows(NotFoundException.class,
                () -> service.processBooking(1L, 33L, true));

        Assertions.assertThrows(NotFoundException.class,
                () -> service.getBooking(bookerId, 12L));

        Assertions.assertThrows(NotFoundException.class,
                () -> service.getUserBookings(10L, BookingState.FUTURE));

        Assertions.assertThrows(NotFoundException.class,
                () -> service.getOwnerBookings(10L, BookingState.FUTURE));

    }

    @Test
    @Rollback
    @DisplayName("Букинг должен корректно создаваться")
    void createTest() {

        BookingDto bookingDto = service.create(createDto);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id=:id", Booking.class);
        query.setParameter("id", bookingDto.getId());

        Assertions.assertEquals(bookingDto, mapper.mapToBookingDto(query.getSingleResult()));

    }


    @Test
    @Rollback
    @DisplayName("Букинг должен корректно обрабатываться")
    void processBooking() {

        BookingDto bookingDto = service.create(createDto);

        BookingDto processedBooking = service.processBooking(item1OwnerId, bookingDto.getId(), true);

        Assertions.assertEquals(processedBooking.getStatus(), BookingStatus.APPROVED);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id=:id", Booking.class);
        query.setParameter("id", bookingDto.getId());

        Assertions.assertEquals(mapper.mapToBookingDto(query.getSingleResult()).getStatus(), BookingStatus.APPROVED);

    }

    @Test
    @DisplayName("Букинг должен возвращаться по id")
    void getBooking() {

        BookingDto bookingDto = service.getBooking(bookerId, booking1.getId());

        Assertions.assertEquals(bookingDto, booking1);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id=:id", Booking.class);
        query.setParameter("id", booking1.getId());

        Assertions.assertEquals(bookingDto, mapper.mapToBookingDto(query.getSingleResult()));

    }

    @Test
    @DisplayName("Список всех аренд владельца должен возвращать корректные значения")
    void getOwnerBookings() {

        List<BookingDto> bookingDtos = service.getOwnerBookings(1L, BookingState.ALL);


        Assertions.assertEquals(bookingDtos, List.of(booking2));

        TypedQuery<Booking> query = em.createQuery("select b " +
                "from Booking as b " +
                "join b.item as it " +
                "join it.owner as o " +
                "where o.id = :id", Booking.class);
        query.setParameter("id", 1L);

        Assertions.assertEquals(bookingDtos, query.getResultList().stream().map(mapper::mapToBookingDto).toList());
    }

    @Test
    @DisplayName("Список всех аренд пользователя должен возвращать корректные значения")
    void getUserBookings() {

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.ALL);

        Assertions.assertEquals(bookingDtos.reversed(), allBookings, "Список возвращаемых букингов не совпадает" +
                " с настоящим, или сортируется неправильно");

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id", Booking.class);
        query.setParameter("id", bookerId);

        Assertions.assertEquals(bookingDtos.reversed(), query.getResultList().stream().map(mapper::mapToBookingDto).toList());
    }

    @Test
    @Rollback
    @DisplayName("Список прошлых аренд пользователя должен возвращать корректные значения")
    void getUserPastBookingsTest() {
        LocalDateTime now = LocalDateTime.now();
        service.create(createDto);

        BookingCreateDto createPastRejectedDto = new BookingCreateDto(
                1L,
                3L,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 2, 1, 0, 0, 0)

        );
        BookingDto bookingDto = service.create(createPastRejectedDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.PAST);

        Assertions.assertEquals(bookingDtos.reversed(), allBookings);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id", Booking.class);
        query.setParameter("id", bookerId);
        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getEnd().isBefore(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .toList();

        Assertions.assertEquals(bookingDtos, found.reversed());

    }

    @Test
    @Rollback
    @DisplayName("Список прошлых аренд владельца должен возвращать корректные значения")
    void getOwnerPastBookingsTest() {
        service.create(createDto);

        BookingCreateDto createPastRejectedDto = new BookingCreateDto(
                1L,
                3L,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 2, 1, 0, 0, 0)

        );
        BookingDto bookingDto = service.create(createPastRejectedDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> bookingDtos = service.getOwnerBookings(item1OwnerId, BookingState.PAST);

        Assertions.assertEquals(bookingDtos.reversed(), List.of(booking2));

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.item as it " +
                        "join it.owner as o " +
                        "where o.id = :id", Booking.class
        );
        query.setParameter("id", item1OwnerId);
        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getEnd().isBefore(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .toList();

        Assertions.assertEquals(bookingDtos, found.reversed());

    }

    @Test
    @Rollback
    @DisplayName("Список будущих аренд владельца должен возвращать корректные значения")
    void getOwnerFutureBookingsTest() {
        service.create(createDto);
        LocalDateTime now = LocalDateTime.now();

        BookingCreateDto createFutureRejectedDto = new BookingCreateDto(
                1L,
                3L,
                LocalDateTime.of(2026, 1, 1, 0, 0, 0),
                LocalDateTime.of(2027, 2, 1, 0, 0, 0)

        );
        BookingDto bookingDto = service.create(createFutureRejectedDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        List<BookingDto> bookingDtos = service.getOwnerBookings(item1OwnerId, BookingState.FUTURE);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.item as it " +
                        "join it.owner as o " +
                        "where o.id = :id", Booking.class
        );
        query.setParameter("id", item1OwnerId);
        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getStart().isAfter(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .toList();

        Assertions.assertEquals(bookingDtos.reversed(), found);
    }

    @Test
    @Rollback
    @DisplayName("Список будущих аренд пользователя должен возвращать корректные значения")
    void getUserFutureBookingsTest() {

        service.create(createDto);
        LocalDateTime now = LocalDateTime.now();

        BookingCreateDto createFutureRejectedDto = new BookingCreateDto(
                1L,
                3L,
                LocalDateTime.of(2026, 1, 1, 0, 0, 0),
                LocalDateTime.of(2027, 2, 1, 0, 0, 0)

        );
        BookingDto bookingDto = service.create(createFutureRejectedDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.FUTURE);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id", Booking.class
        );
        query.setParameter("id", bookerId);
        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getStart().isAfter(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .toList();

        Assertions.assertEquals(bookingDtos.reversed(), found);


    }

    @Test
    @Rollback
    @DisplayName("Список текущих аренд пользователя должен возвращать корректные значения")
    void getUserCurrentBookingsTest() {
        service.create(createCurrentDto);
        LocalDateTime now = LocalDateTime.now();

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.CURRENT);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id", Booking.class
        );
        query.setParameter("id", bookerId);

        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .collect(Collectors.toList());


        Assertions.assertEquals(bookingDtos.reversed(), found);
    }

    @Test
    @Rollback
    @DisplayName("Список текущих аренд владельца должен возвращать корректные значения")
    void getOwnerCurrentBookingsTest() {
        service.create(createCurrentDto);
        LocalDateTime now = LocalDateTime.now();


        List<BookingDto> bookingDtos = service.getOwnerBookings(item1OwnerId, BookingState.CURRENT);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.item as it " +
                        "join it.owner as o " +
                        "where o.id = :id", Booking.class
        );
        query.setParameter("id", item1OwnerId);

        List<BookingDto> found = query.getResultList().stream().filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now)
                        && booking.getStatus() != BookingStatus.CANCELED
                        && booking.getStatus() != BookingStatus.REJECTED)
                .map(mapper::mapToBookingDto)
                .toList();


        Assertions.assertEquals(bookingDtos.reversed(), found);


    }

    @Test
    @Rollback
    @DisplayName("Список ожидающих аренд пользователя должен возвращать корректные значения")
    void getOwnerWaitingBookingsTest() {
        service.create(createDto);
        service.create(createCurrentDto);

        List<BookingDto> bookingDtos = service.getOwnerBookings(item1OwnerId, BookingState.WAITING);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.item as it " +
                        "join it.owner as o " +
                        "where o.id = :id and b.status in :statuses", Booking.class
        );
        query.setParameter("id", item1OwnerId);
        query.setParameter("statuses", List.of(BookingStatus.WAITING));

        List<BookingDto> found = query.getResultList().stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());


        Assertions.assertEquals(bookingDtos, found);
    }

    @Test
    @Rollback
    @DisplayName("Список ожидающих аренд пользователя должен возвращать корректные значения")
    void getUserWaitingBookingsTest() {
        service.create(createDto);
        service.create(createCurrentDto);

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.WAITING);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id and b.status in :statuses", Booking.class
        );

        query.setParameter("id", bookerId);
        query.setParameter("statuses", List.of(BookingStatus.WAITING));

        List<BookingDto> found = query.getResultList().stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Assertions.assertEquals(bookingDtos, found);
    }

    @Test
    @Rollback
    @DisplayName("Список отклоненных аренд пользователя должен возвращать корректные значения")
    void getUserRejectedBookingsTest() {
        BookingDto bookingDto = service.create(createDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        List<BookingDto> bookingDtos = service.getUserBookings(bookerId, BookingState.REJECTED);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.booker as bkr " +
                        "where bkr.id = :id and b.status in :statuses", Booking.class
        );

        query.setParameter("id", bookerId);
        query.setParameter("statuses", List.of(BookingStatus.CANCELED, BookingStatus.REJECTED));

        List<BookingDto> found = query.getResultList().stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Assertions.assertEquals(bookingDtos, found);

    }

    @Test
    @Rollback
    @DisplayName("Список отклоненных аренд владельца должен возвращать корректные значения")
    void getOwnerRejectedBookingsTest() {
        BookingDto bookingDto = service.create(createDto);
        service.processBooking(item1OwnerId, bookingDto.getId(), false);

        List<BookingDto> bookingDtos = service.getOwnerBookings(item1OwnerId, BookingState.REJECTED);

        TypedQuery<Booking> query = em.createQuery(
                "select b " +
                        "from Booking as b " +
                        "join b.item as it " +
                        "join it.owner as o " +
                        "where o.id = :id and b.status in :statuses", Booking.class
        );
        query.setParameter("id", item1OwnerId);
        query.setParameter("statuses", List.of(BookingStatus.CANCELED, BookingStatus.REJECTED));

        List<BookingDto> found = query.getResultList().stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());


        Assertions.assertEquals(bookingDtos, found);


    }
}