package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

@Data
public class NewBookingRequest {

    @NotBlank(message = "Дата начала бронирования не может быть пустым")
    @Future(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDate startDate;
    @NotBlank(message = "Дата окончания бронирования не может быть пустым")
    @Future(message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDate endDate;
    private Item item;
}
