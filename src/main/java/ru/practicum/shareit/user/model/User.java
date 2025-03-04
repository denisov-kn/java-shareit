package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", schema = "public")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;
}
