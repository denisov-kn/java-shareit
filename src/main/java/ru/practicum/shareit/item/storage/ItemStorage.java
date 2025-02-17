package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item getItemByUserIdAndItemId(Long itemId, Long userId);

    Collection<Item> getAllItemsByUserId(Long userId);

    Collection<Item> searchItemsByNameAndDescription(String text, Long userId);

    Item saveItem(Item item);

    Item updateItem(Item item);

    boolean isItemOwnerByUserId(Long userId, Long itemId);
}
