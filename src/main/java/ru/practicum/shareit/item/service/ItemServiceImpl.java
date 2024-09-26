package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper mapper;

    @Override
    public ItemDto create(ItemCreateDto itemDto) {
        User user = userRepository.getById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));

        Item item = mapper.mapCreateDtoToItem(itemDto);
        item.setOwner(user);
        return mapper.mapToItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemRepository.getById(itemId)
                .map(mapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemId + " не найден"));
    }

    @Override
    public ItemDto update(ItemUpdateDto itemDto) {
        userRepository.getById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));
        Item updatedItem = itemRepository.getById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemDto.getId() + " не найден"));

        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }

        return mapper.mapToItemDto(itemRepository.update(updatedItem));
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        return itemRepository.getUserItems(userId).stream()
                .map(mapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchAvailableItems(Long userId, String text) {
        return itemRepository.search(userId, text).stream()
                .map(mapper::mapToItemDto)
                .toList();
    }
}
