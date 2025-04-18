package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.comments.CommentDto;

import java.util.Collection;

@Data
public class ItemCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Collection<CommentDto> comments;
}
