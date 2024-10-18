package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDto {
    Long id;
    String text;
    Timestamp created;
    String authorName;

}
