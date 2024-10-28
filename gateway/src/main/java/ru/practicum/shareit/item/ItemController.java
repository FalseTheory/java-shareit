package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody ItemCreateDto itemCreateDto) {

        return itemClient.createItem(userId, itemCreateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Positive @PathVariable Long itemId) {

        return itemClient.getItemById(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemUpdateDto itemUpdateDto,
                                             @Positive @PathVariable Long itemId) {


        return itemClient.updateItem(userId, itemUpdateDto, itemId);

    }

    @GetMapping
    public ResponseEntity<Object> getItemsForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "") String text) {
        return itemClient.searchAvailableItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable @Positive Long itemId,
                                              @Valid @RequestBody CommentCreateDto commentCreateDto) {

        return itemClient.postComment(userId, itemId, commentCreateDto);
    }

}
