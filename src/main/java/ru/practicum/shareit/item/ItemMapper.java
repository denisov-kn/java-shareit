package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
public class ItemMapper {
    public static Item mapToItem(NewItemRequest request) {
        Item item = new Item();
        item.setAvailable(request.getAvailable());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
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
}
