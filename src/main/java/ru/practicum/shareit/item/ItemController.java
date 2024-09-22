package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Shared-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Shared-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemDto);
    }

    @GetMapping
    public List<ItemDto> getItemsForUser(@RequestHeader("X-Shared-User-Id") Long userId) {
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestHeader("X-Shared-User-Id") Long userId,
                                              @RequestParam(required = true) String text) {
        return itemService.searchAvailableItems(userId, text);
    }

}
