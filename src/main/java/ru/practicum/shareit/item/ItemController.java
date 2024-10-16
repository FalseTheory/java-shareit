package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("trying to create item - {} for user - {}", itemCreateDto, userId);
        itemCreateDto.setOwnerId(userId);
        ItemDto itemDto = itemService.create(itemCreateDto);
        log.info("item created successfully");
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @Positive @PathVariable Long itemId) {
        log.info("getting item with id - {}", itemId);
        return itemService.getById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemUpdateDto itemUpdateDto,
                              @Positive @PathVariable Long itemId) {
        log.info("trying to update item with id - {}, new item - {}", itemId, itemUpdateDto);
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setOwnerId(userId);
        ItemDto itemDto = itemService.update(itemUpdateDto);
        log.info("item updated successfully");
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getItemsForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("getting all items for user - {}", userId);
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "") String text) {
        log.info("searching available items with search query - {}", text);
        List<ItemDto> searchResult = itemService.searchAvailableItems(userId, text);
        log.info("search successful");
        return searchResult;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable @Positive Long itemId,
                                  @Valid @RequestBody CommentCreateDto commentCreateDto) {
        commentCreateDto.setItemId(itemId);
        commentCreateDto.setUserId(userId);
        return itemService.postComment(commentCreateDto);
    }

}
