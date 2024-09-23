package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto getById(Long itemId);

    ItemDto update(Long userId, ItemDto itemDto, Long itemId);

    List<ItemDto> getItemsForUser(Long userId);

    List<ItemDto> searchAvailableItems(Long userId, String text);
}
