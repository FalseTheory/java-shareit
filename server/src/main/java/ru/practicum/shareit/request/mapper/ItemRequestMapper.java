package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "owner", ignore = true)
    ItemRequest mapCreateDtoToItemRequest(ItemRequestCreateDto itemRequestCreateDto);

    @Mapping(target = "ownerId", source = "itemRequest.owner.id")
    @Mapping(target = "items", source = "itemList")
    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<Item> itemList);

    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "items", source = "itemList")
    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<Item> itemList, Long ownerId);


}
