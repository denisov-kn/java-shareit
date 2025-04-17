package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import java.util.Collection;

@NoArgsConstructor
public class ItemMapper {
    public static Item mapToItem(NewItemRequest request, User user, ItemRequest itemRequest) {
        Item item = new Item();
        item.setAvailable(request.getAvailable());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setOwner(user);
        item.setItemRequest(itemRequest);
        item.setItemRequest(itemRequest);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemCommentDateDto mapToItemCommentDateDto(Item item,
                                                             Collection<CommentDto> comments,
                                                             BookingInfoDto nextBooking,
                                                             BookingInfoDto lastBooking) {
        return ItemCommentDateDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .nextBooking(nextBooking)
                .lastBooking(lastBooking)
                .build();

    }
}
