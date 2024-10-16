package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment as c " +
            "join c.item as it " +
            "join c.author as a " +
            "where it.id = ?1")
    List<Comment> findItemComments(Long itemId);

    @Override
    @EntityGraph(attributePaths = {"item"})
    List<Comment> findAllById(Iterable<Long> longs);
}
