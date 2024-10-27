package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceTest {
    private final EntityManager em;
    private final ItemMapper mapper;
    private final ItemService service;
    private final CommentMapper commentMapper;

    private final long item1UserId = 1L;

    private final ItemDto item1 = new ItemDto(
            1L,
            "item1",
            "desc1",
            true,
            new ArrayList<>(),
            new ItemDto.BookingShortDto(2L, 3L),
            null
    );
    private final ItemDto item2 = new ItemDto(
            2L,
            "item2",
            "desc2",
            true,
            new ArrayList<>(),
            null,
            null
    );
    private final ItemCreateDto newItem = new ItemCreateDto(
            "item3",
            "desc3",
            true,
            2L,
            null
    );
    private final CommentCreateDto newComment = new CommentCreateDto(
            "nice item",
            3L,
            item2.getId()
    );
    private final ItemCreateDto newItemWithUnknownRequest = new ItemCreateDto(
            "item3",
            "desc3",
            true,
            2L,
            6L
    );
    private final ItemUpdateDto updateDto = new ItemUpdateDto(
            item1.getId(),
            item1UserId,
            "updatedName",
            "updatedDesc",
            false
    );
    private final List<ItemDto> allItems = List.of(item1, item2);

    @Test
    @Rollback
    @DisplayName("Предмет должен создаваться в БД")
    void createTest() {
        ItemDto itemDto = service.create(newItem);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id=:id", Item.class);
        query.setParameter("id", 3L);

        Assertions.assertEquals(itemDto, mapper.mapToItemDto(query.getSingleResult()));
    }

    @Test
    @Rollback
    @DisplayName("Тесты исключений")
    void ExceptionsTest() {
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                "desc",
                2L,
                1L
        );
        Assertions.assertThrows(NotFoundException.class,
                () -> service.create(newItemWithUnknownRequest));
        Assertions.assertThrows(UnavailableException.class,
                () -> service.postComment(commentCreateDto));
    }

    @Test
    @DisplayName("Предмет должен корректно возвращаться из бд с букингами")
    void getByIdTest() {
        ItemDto itemDto = service.getById(item1.getId(), item1UserId);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id=:id", Item.class);
        query.setParameter("id", item1.getId());
        TypedQuery<Booking> queryB = em.createQuery("select b From Booking b join b.item as i where i.id=:id",
                Booking.class);
        queryB.setParameter("id", itemDto.getId());

        Assertions.assertEquals(itemDto, mapper.mapToItemDto(query.getSingleResult()
                , queryB.getResultList(), new ArrayList<>()));
    }

    @Test
    @Rollback
    @DisplayName("Предмет должен корректно обновляться")
    void updateTest() {
        ItemDto itemDto = service.update(updateDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id=:id", Item.class);
        query.setParameter("id", item1.getId());

        Assertions.assertEquals(itemDto, mapper.mapToItemDto(query.getSingleResult()));

    }

    @Test
    @DisplayName("Все предметы пользователя должны корректно возвращаться из БД")
    void getItemsForUserTest() {

        List<ItemDto> userList = service.getItemsForUser(item1UserId);
        TypedQuery<Item> query = em.createQuery("select it from Item it " +
                "join it.owner as o where o.id=:id", Item.class);
        query.setParameter("id", item1UserId);

        Map<Long, List<Booking>> bookingMap = new HashMap<>();

        for (ItemDto dto : userList) {
            TypedQuery<Booking> queryB = em.createQuery("select b From Booking b join b.item as i where i.id=:id",
                    Booking.class);
            queryB.setParameter("id", dto.getId());
            bookingMap.put(dto.getId(), queryB.getResultList());
        }


        assertEquals(userList, query.getResultList().stream().map(item -> mapper.mapToItemDto(item,
                bookingMap.get(item.getId()), new ArrayList<>())).toList());
    }

    @Test
    @DisplayName("Поиск должен правильно работать")
    void searchAvailableItemsTest() {
        String searchString = "item2";
        long item2UserId = 2L;
        List<ItemDto> searchList = service.searchAvailableItems(item2UserId, searchString);

        String SEARCH_QUERY = "select it " +
                "from Item as it " +
                "join it.owner as o " +
                "where o.id != :id and it.available = TRUE " +
                "and (LOWER(it.name) like LOWER(:text) or LOWER(it.description) like LOWER(:text))";
        TypedQuery<Item> query = em.createQuery(SEARCH_QUERY, Item.class);
        query.setParameter("id", item2UserId);
        query.setParameter("text", searchString);

        assertEquals(searchList, query.getResultList().stream().map(item -> mapper.mapToItemDto(item,
                null, new ArrayList<>())).toList());


    }

    @Test
    @Rollback
    @DisplayName("Коммент должен создаваться если пользователь брал аренду")
    void postCommentTest() {

        CommentDto commentDto = service.postComment(newComment);

        TypedQuery<Comment> query = em.createQuery("select c from Comment c " +
                "join c.author as a where a.id=:id", Comment.class);
        query.setParameter("id", newComment.getUserId());

        assertEquals(commentDto, commentMapper.mapToCommentDto(query.getSingleResult()));


    }
}