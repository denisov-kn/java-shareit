package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.user.User;
import java.util.Collection;

@NoArgsConstructor
public class ItemMapper {
    public static Item mapToItem(NewItemRequest request, User user) {
        Item item = new Item();
        item.setAvailable(request.getAvailable());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setOwner(user);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemCommentDto mapToItemCommentDto(Item item, Collection<CommentDto> comments) {
        ItemCommentDto itemCommentDto = new ItemCommentDto();
        itemCommentDto.setId(item.getId());
        itemCommentDto.setName(item.getName());
        itemCommentDto.setDescription(item.getDescription());
        itemCommentDto.setAvailable(item.getAvailable());
        itemCommentDto.setComments(comments);
        return itemCommentDto;
    }

    public static ItemCommentDateDto mapToItemCommentDateDto(Item item,
                                                             Collection<CommentDto> comments,
                                                             BookingInfoDto nextBooking,
                                                             BookingInfoDto lastBooking) {
        ItemCommentDateDto itemCommentDateDto = new ItemCommentDateDto();
        itemCommentDateDto.setId(item.getId());
        itemCommentDateDto.setName(item.getName());
        itemCommentDateDto.setDescription(item.getDescription());
        itemCommentDateDto.setAvailable(item.getAvailable());
        itemCommentDateDto.setComments(comments);
        itemCommentDateDto.setNextBooking(nextBooking);
        itemCommentDateDto.setLastBooking(lastBooking);
        return itemCommentDateDto;

    }
}
