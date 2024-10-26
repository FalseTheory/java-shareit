package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data @AllArgsConstructor
public class CommentDto {
    Long id;
    String text;
    Timestamp created;
    String authorName;

}
