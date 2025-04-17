package ru.practicum.shareit.item.comments;

public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .itemId(comment.getItemId())
                .created(comment.getCreated())
                .build();
    }
}
