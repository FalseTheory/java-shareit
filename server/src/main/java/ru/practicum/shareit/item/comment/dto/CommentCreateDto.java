package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class CommentCreateDto {
    String text;
    Long userId;
    Long itemId;
}
