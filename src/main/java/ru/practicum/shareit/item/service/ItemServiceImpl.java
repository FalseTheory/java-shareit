package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        userRepository.getById(userId)
                .orElseThrow(()->new NotFoundException("Пользователь с id - " + userId + " не найден"));

        return ItemMapper.mapToItemDto(itemRepository.create(ItemMapper.mapToItem(itemDto), userId));
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemRepository.getById(itemId)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(()->new NotFoundException("Предмет с id - " + itemId + " не найден"));
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto, Long itemId) {
        userRepository.getById(userId)
                .orElseThrow(()->new NotFoundException("Пользователь с id - " + userId + " не найден"));
        itemRepository.getById(itemId)
                .orElseThrow(()->new NotFoundException("Предмет с id - " + itemId + " не найден"));
        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(itemId);

        return ItemMapper.mapToItemDto(itemRepository.update(userId, item));
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchAvailableItems(Long userId, String text) {
        return itemRepository.search(userId, text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
