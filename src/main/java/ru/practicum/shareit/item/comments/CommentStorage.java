package ru.practicum.shareit.item.comments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommentStorage extends JpaRepository<Comment, Long> {

    Collection<Comment> searchCommentsByItemId(long itemId);

    Collection<Comment> searchCommentByItemIdIn(Collection<Long> itemIds);
}
