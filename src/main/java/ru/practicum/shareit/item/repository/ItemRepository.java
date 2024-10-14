package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("select it " +
            "from Item as it " +
            "join it.owner as o " +
            "where o.id = ?1")
    List<Item> getUserItems(Long userId);


    @Query("select it " +
            "from Item as it " +
            "join it.owner as o " +
            "where o.id != ?1 and it.available = TRUE " +
            "and (LOWER(it.name) like LOWER(?2) or LOWER(it.description) like LOWER(?2))")
    List<Item> search(Long userId, String text);
}
