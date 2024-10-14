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
            "where o.id = ?1")
    List<Booking> findOwnerBookings(Long userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as bkr " +
            "where bkr.id = ?1")
    @EntityGraph(attributePaths = {"item", "item.owner", "booker"})
    List<Booking> findUserBookings(Long userId);


}
