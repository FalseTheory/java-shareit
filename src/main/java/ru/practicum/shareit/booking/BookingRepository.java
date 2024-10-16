package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Override
    @EntityGraph(attributePaths = {"item", "booker", "item.owner"})
    Optional<Booking> findById(Long aLong);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as it " +
            "join it.owner as o " +
            "where o.id = ?1 " +
            "order by b.start desc")
    List<Booking> findOwnerBookings(Long userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as it " +
            "where it.id = ?1")
    List<Booking> findBookingsForItem(Long itemId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as it " +
            "join it.owner as o " +
            "where o.id = ?1 and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findOwnerBookings(Long userId, BookingStatus state);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as bkr " +
            "where bkr.id = ?1 " +
            "order by b.start desc")
    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    List<Booking> findUserBookings(Long userId);


    @Query("select b " +
            "from Booking as b " +
            "join b.booker as bkr " +
            "where bkr.id = ?1 and b.status = ?2 " +
            "order by b.start desc")
    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    List<Booking> findUserBookings(Long userId, BookingStatus state);


}
