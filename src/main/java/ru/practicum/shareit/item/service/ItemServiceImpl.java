package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService{
    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto getById(Long itemId) {
        return null;
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        return null;
    }

    @Override
    public List<ItemDto> searchAvailableItems(Long userId, String text) {
        return null;
    }
}
