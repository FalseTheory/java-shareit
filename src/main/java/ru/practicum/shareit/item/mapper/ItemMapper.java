package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setId(item.getId());

        return itemDto;
    }

    public Item mapCreateDtoToItem(ItemCreateDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setOwnerId(itemDto.getOwnerId());

        return item;

    }

    public Item mapUpdateDtoToItem(ItemUpdateDto itemUpdateDto) {
        Item item = new Item();
        item.setOwnerId(itemUpdateDto.getOwnerId());
        item.setId(itemUpdateDto.getId());
        item.setName(itemUpdateDto.getName());
        item.setDescription(itemUpdateDto.getDescription());
        item.setAvailable(itemUpdateDto.getAvailable());

        return item;

    }
}
