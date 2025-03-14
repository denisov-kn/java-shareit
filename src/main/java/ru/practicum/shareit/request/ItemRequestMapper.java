package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

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
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static ItemRequestItemsDto mapToItemRequestItemsDto(ItemRequest itemRequest, Collection<ItemDto> items) {
        ItemRequestItemsDto itemRequestItemsDto = new ItemRequestItemsDto();
        itemRequestItemsDto.setId(itemRequest.getId());
        itemRequestItemsDto.setDescription(itemRequest.getDescription());
        itemRequestItemsDto.setCreated(itemRequest.getCreated());
        itemRequestItemsDto.setItems(items);
        return itemRequestItemsDto;
    }
}
