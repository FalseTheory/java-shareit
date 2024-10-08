package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemCreateDto itemDto);

    ItemDto getById(Long itemId);

    ItemDto update(ItemUpdateDto itemDto);

    List<ItemDto> getItemsForUser(Long userId);

    List<ItemDto> searchAvailableItems(Long userId, String text);
}
