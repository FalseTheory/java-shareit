package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    String text;
    Long userId;
    Long itemId;
}
