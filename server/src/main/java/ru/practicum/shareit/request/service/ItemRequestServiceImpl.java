package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto add(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден"));

        ItemRequest itemRequest = itemRequestMapper.mapCreateDtoToItemRequest(itemRequestCreateDto);
        itemRequest.setOwner(user);
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequestMapper.mapToItemRequestDto(requestRepository.save(itemRequest), null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllUserRequest(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден"));
        List<Item> itemsForRequests = itemRepository.getAllItemsForRequests();
        List<ItemRequest> requests = requestRepository.findAllUserRequests(userId);
        List<Long> ids = requests.stream().map(ItemRequest::getId).toList();
        Map<Long, List<Item>> itemMap = new HashMap<>();

        for (Long id : ids) {
            itemMap.put(id, new ArrayList<>());
        }
        for (Item item : itemsForRequests) {
            Long id = item.getRequest().getId();
            if (ids.contains(id)) {
                itemMap.get(id).add(item);
            }
        }


        return requests.stream()
                .map(itemRequest -> itemRequestMapper.mapToItemRequestDto(itemRequest,
                        itemMap.get(itemRequest.getId()), userId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllRequests() {
        List<Item> itemsForRequests = itemRepository.getAllItemsForRequests();
        List<ItemRequest> requests = requestRepository.findAll();
        List<Long> ids = requests.stream().map(ItemRequest::getId).toList();
        Map<Long, List<Item>> itemMap = new HashMap<>();

        for (Long id : ids) {
            itemMap.put(id, new ArrayList<>());
        }
        for (Item item : itemsForRequests) {
            Long id = item.getRequest().getId();
            if (ids.contains(id)) {
                itemMap.get(id).add(item);
            }
        }

        return requests.stream()
                .map(itemRequest -> itemRequestMapper.mapToItemRequestDto(itemRequest,
                        itemMap.get(itemRequest.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto get(Long id) {

        ItemRequest itemRequest = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id - " + id + " не найден"));

        return itemRequestMapper.mapToItemRequestDto(itemRequest,
                itemRequest.getItems());
    }
}
