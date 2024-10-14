package ru.practicum.shareit.item.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {


    @Mapping(target = "authorName", source = "author.name")
    CommentDto mapToCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "item", ignore = true)
    Comment mapCreateDtoToComment(CommentCreateDto commentCreateDto);
}
