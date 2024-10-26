package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден"));


        return requestRepository.findAllUserRequests(userId).stream()
                .map(itemRequest -> itemRequestMapper.mapToItemRequestDto(itemRequest, null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(itemRequest -> itemRequestMapper.mapToItemRequestDto(itemRequest, null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto get(Long id) {

        ItemRequest itemRequest = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id - " + id + " не найден"));

        return itemRequestMapper.mapToItemRequestDto(itemRequest,
                itemRequest.getItems().stream().map(itemMapper::mapToItemDto).toList());
    }
}
