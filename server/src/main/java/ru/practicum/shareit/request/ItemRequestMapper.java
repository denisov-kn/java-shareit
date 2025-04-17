package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(NewItemRequestDto requestDto, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .id(itemRequest.getId())
                .build();
    }

    public static ItemRequestItemsDto mapToItemRequestItemsDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestItemsDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .id(itemRequest.getId())
                .items(items)
                .build();
    }
}
