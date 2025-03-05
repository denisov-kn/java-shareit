package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "bookings", schema = "public")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "item_id")
    private Item item;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "booker_id")
    private User booker;

    @Column(name = "status", nullable = false)
    private Status status;
}
