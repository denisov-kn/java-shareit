package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Item item;
    private Status status;
}
