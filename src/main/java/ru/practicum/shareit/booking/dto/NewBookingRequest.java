package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NewBookingRequest {

    @NotNull(message = "Дата начала бронирования не может быть пустым")
    @Future(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания бронирования не может быть пустым")
    @Future(message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
