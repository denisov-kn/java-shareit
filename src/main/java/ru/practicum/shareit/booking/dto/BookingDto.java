package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;


@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Status status;
    public UserDto booker;
}
