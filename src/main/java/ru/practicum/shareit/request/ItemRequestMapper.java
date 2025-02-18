package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.time.LocalDate;

@NoArgsConstructor
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(NewItemRequestDto requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDate.now());
        itemRequest.setDescription(requestDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequestorId(itemRequest.getRequestorId());
        return itemRequestDto;
    }
}
