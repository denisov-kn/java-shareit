package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemStorageMemory implements ItemStorage {

    public final Map<Long, Item> items = new HashMap<Long, Item>();

    @Override
    public Item getItemByUserIdAndItemId(Long itemId, Long userId) {
        Item item =  getItemById(itemId);
        if (item.getOwnerId().equals(userId))
            return item;
        else throw new NotFoundException("Вещь с ID - " + itemId + " не принадлежит пользователю с ID - " + userId);
    }

    @Override
    public Collection<Item> getAllItemsByUserId(Long userId) {
       Collection<Item> usersItems =  items.values().stream()
               .filter(item -> item.getOwnerId().equals(userId))
               .toList();
       if(usersItems.isEmpty())
           throw new NotFoundException("У пользователя с id - " + userId + " нет вещей во владении");
       return usersItems;
    }

    @Override
    public Collection<Item> searchItemsByNameAndDescription(String text, Long userId) {
        Collection<Item> usersItems = items.values().stream()
                .filter(item -> item.getAvailable().equals(true)
                        && (item.getDescription().contains(text) || item.getName().contains(text))
                )
                .toList();
        if (usersItems.isEmpty())
            throw new NotFoundException("Нет доступных вещей с таким описанием: " + text);
        return usersItems;
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }


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
