package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemStorage itemStorage;
    public final UserStorage userStorage;

    public ItemDto getItemById(Long itemId, Long userId) {
        checkUser(userId);
        return ItemMapper.mapToItemDto(itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"))
        );
    }

    public Collection<ItemDto> getAllItemsByUserId(Long userId) {
        checkUser(userId);
        return itemStorage.getAllItemsByOwnerId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItems(String text, Long userId) {
        if (text.isEmpty())
            return new ArrayList<>();
        checkUser(userId);
        return itemStorage.searchItemsByNameAndDescription(text).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItem(Long userId, NewItemRequest request) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = ItemMapper.mapToItem(request, user);
        item = itemStorage.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Long userId, Long itemId,  UpdateItemRequest request) {
        checkUser(userId);
        if (!itemStorage.existsByIdAndOwnerId(itemId, userId))
            throw new NotFoundException("Вещь с ID: " + itemId + " не принадлежит пользователю с ID: " + userId);

        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }
        item = itemStorage.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    private void checkUser(Long userId) {
        userStorage.findById(userId).
                orElseThrow(() -> new NotFoundException("User not found"));
    }

}
