package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateItemRequest {
    private String name;
    private String description;
    private Boolean available;
}
