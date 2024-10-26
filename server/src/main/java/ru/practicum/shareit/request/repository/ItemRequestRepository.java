package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Override
    @EntityGraph(attributePaths = {"owner", "items"})
    Optional<ItemRequest> findById(Long aLong);

    @Query("select rq " +
            "from ItemRequest as rq " +
            "join rq.owner as o " +
            "where o.id = ?1")
    List<ItemRequest> findAllUserRequests(Long userId);
}
