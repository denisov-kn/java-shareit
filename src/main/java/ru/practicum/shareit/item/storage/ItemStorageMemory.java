package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemStorageMemory implements ItemStorage {

    public final Map<Long, Item> items = new HashMap<Long, Item>();

    @Override
    public Collection<Item> getAllItemsByUserId(Long userId) {
       Collection<Item> usersItems =  items.values().stream()
               .filter(item -> item.getOwnerId().equals(userId))
               .toList();
       if (usersItems.isEmpty())
           throw new NotFoundException("У пользователя с id - " + userId + " нет вещей во владении");
       return usersItems;
    }

    @Override
    public Collection<Item> searchItemsByNameAndDescription(String text, Long userId) {

        String lowText = text.toLowerCase();
        Collection<Item> usersItems = items.values().stream()
                .filter(item -> item.getAvailable()
                        && (item.getDescription().toLowerCase().contains(lowText)
                        || item.getName().toLowerCase().contains(lowText))
                )
                .toList();
        if (usersItems.isEmpty())
            return new ArrayList<>();
        return usersItems;
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
       Item updatedItem = items.get(item.getId());
       return ItemMapper.updateItemFields(item, updatedItem);
    }

    @Override
    public boolean isItemOwnerByUserId(Long userId, Long itemId) {
        checkItem(itemId);
        if (items.containsKey(itemId)) {
           Item item = items.get(itemId);
           return item.getOwnerId().equals(userId);
        }
        return false;
    }

    @Override
    public Item getItemById(Long itemId) {
        checkItem(itemId);
        return items.get(itemId);
    }

    private void checkItem(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с ID - " + itemId + " не найдена");
        }
    }

    private long getId() {
        long lastId = items.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
        return lastId + 1;
    }

}
