package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    Long id;
    LocalDate startDate;
    LocalDate endDate;
    Item item;
    Long bookerId;
    Status status;
}
