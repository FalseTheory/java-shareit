package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDto add(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllUserRequest(Long userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return null;
    }

    @Override
    public ItemRequestDto get(Long id) {
        return null;
    }
}
