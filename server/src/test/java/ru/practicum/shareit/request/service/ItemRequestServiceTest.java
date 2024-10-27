package ru.practicum.shareit.request.service;

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

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemRequestServiceTest {

    private final EntityManager em;
    private final ItemRequestService service;
    private final ItemRequestMapper mapper;

    private final ItemRequestDto request1 = new ItemRequestDto(
            1L,
            3L,
            "need nice item",
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            List.of(new ItemDto(3L, "item3", "desc3",
                    true, null, null, null))
    );
    private final ItemRequestDto request2 = new ItemRequestDto(
            2L,
            3L,
            "need more items",
            LocalDateTime.of(2023, 2, 1, 0, 0, 0),
            new ArrayList<>()
    );
    private final List<ItemRequestDto> allRequests = List.of(request1, request2);
    private final ItemRequestCreateDto createDto = new ItemRequestCreateDto("new request");

    @Test
    @Rollback
    @DisplayName("Запрос должен корректно добавляться в базу данных")
    void addTest() {

        ItemRequestDto created = service.add(3L, createDto);


        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.id=:id",
                ItemRequest.class);
        query.setParameter("id", created.getId());

        Assertions.assertEquals(created, mapper.mapToItemRequestDto(query.getSingleResult(), null));

    }

    @Test
    @DisplayName("Список всех запросов пользователя должен корректно возвращаться")
    void getAllUserRequestTest() {


        List<ItemRequestDto> userRequests = service.getAllUserRequest(request1.getOwnerId());

        Assertions.assertEquals(userRequests, allRequests);

        String queryString = "select rq " +
                "from ItemRequest as rq " +
                "join rq.owner as o " +
                "where o.id = :id";
        TypedQuery<ItemRequest> query = em.createQuery(queryString, ItemRequest.class);
        query.setParameter("id", request1.getOwnerId());

        Assertions.assertEquals(userRequests, query.getResultList().stream()
                .map(itemRequest -> mapper.mapToItemRequestDto(itemRequest, itemRequest.getItems())).toList());


    }

    @Test
    @Rollback
    @DisplayName("Список всех запросов должен быть корректным")
    void getAllRequestsTest() {


        List<ItemRequestDto> allRequests1 = service.getAllRequests();


        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest as r", ItemRequest.class);

        Assertions.assertEquals(allRequests1, query.getResultList().stream()
                .map(itemRequest -> mapper.mapToItemRequestDto(itemRequest, itemRequest.getItems())).toList());
    }

    @Test
    @DisplayName("Запрос должен корректно получаться по id")
    void getTest() {

        ItemRequestDto requestDto = service.get(request1.getId());


        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.id=:id",
                ItemRequest.class);
        query.setParameter("id", requestDto.getId());

        ItemRequest request = query.getSingleResult();

        Assertions.assertEquals(requestDto, mapper.mapToItemRequestDto(request, request.getItems()));
    }
}