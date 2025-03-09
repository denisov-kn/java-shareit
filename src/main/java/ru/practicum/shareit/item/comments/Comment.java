package ru.practicum.shareit.item.comments;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;


    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "author_id")
    private User author;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
