package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemStorage itemStorage;
    public final UserStorage userStorage;

    public ItemDto getItemByUserIdAndItemId(Long itemId, Long userId) {
        return ItemMapper.mapToItemDto(itemStorage.getItemByUserIdAndItemId(itemId, userId));
    }

    public Collection<ItemDto> getAllItemsByUserId(Long userId) {
        userStorage.checkUser(userId);
        return itemStorage.getAllItemsByUserId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItems(String text, Long userId) {
        if (text.isEmpty())
            return new ArrayList<>();
        userStorage.checkUser(userId);
        return itemStorage.searchItemsByNameAndDescription(text, userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItem(Long userId, NewItemRequest request) {
        userStorage.checkUser(userId);
        Item item = ItemMapper.mapToItem(request);
        item.setOwnerId(userId);
        item = itemStorage.saveItem(item);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Long userId, Long itemId,  UpdateItemRequest request) {
        userStorage.checkUser(userId);
        if (!itemStorage.isItemOwnerByUserId(userId, itemId))
            throw new NotFoundException("Вещь с ID: " + itemId + " не принадлежит пользователю с ID: " + userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(request.getAvailable());
        item.setDescription(request.getDescription());
        item.setName(request.getName());

        item = itemStorage.updateItem(item);
        return ItemMapper.mapToItemDto(item);
    }

}
