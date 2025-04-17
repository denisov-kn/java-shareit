package ru.practicum.shareit.item.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Long> {

    Collection<Comment> searchCommentsByItemId(long itemId);

    Collection<Comment> searchCommentByItemIdIn(Collection<Long> itemIds);
}
