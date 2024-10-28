package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto add(Long userId, ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestDto> getAllUserRequest(Long userId);

    List<ItemRequestDto> getAllRequests();

    ItemRequestDto get(Long id);
}
