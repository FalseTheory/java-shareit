package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper mapper;

    @Override
    public ItemDto create(ItemCreateDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));

        Item item = mapper.mapCreateDtoToItem(itemDto);
        item.setOwner(user);
        return mapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(mapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemId + " не найден"));
    }

    @Override
    public ItemDto update(ItemUpdateDto itemDto) {
        userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));
        Item updatedItem = itemRepository.findById(itemDto.getId())
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

        return mapper.mapToItemDto(itemRepository.save(updatedItem));
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        /*решение проблемы 2N+1
            List<Item> items = itemRepository.getUserItems(userId);
            List<Booking> bookings = bookingRepository.findByOwner_Id(items.stream().map(Item::getId()).toList(), LocalDateTime.now());
            bookings -> bookingsMapByItemId
            Map<Long, List<Booking>> bookingsMapByItemId = new HashMap<>();
            items.stream().map(i -> itemMapper.toItemInfoDto(i, getLastBooking(bookingsMapByItemId.get(i.getId())))), getNextBooking(bookingsMapByItemId.get(i.getId())
         */

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
