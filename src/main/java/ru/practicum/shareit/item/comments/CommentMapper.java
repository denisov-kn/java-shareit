package ru.practicum.shareit.item.comments;

public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setItemId(comment.getItemId());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}
