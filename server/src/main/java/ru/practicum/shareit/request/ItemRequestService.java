package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final ItemService itemService;

    public ItemRequestDto create(NewItemRequestDto newItemRequestDto, Long userId) {
        User user = checkUser(userId);
        ItemRequest itemRequest = itemRequestStorage.save(ItemRequestMapper.mapToItemRequest(newItemRequestDto, user));
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    public List<ItemRequestItemsDto> getAllForUser(Long userId) {
        checkUser(userId);
        List<ItemRequest> requests = itemRequestStorage.findByRequestor_Id(userId);
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();
        Collection<Item> itemList = itemStorage.searchItemsByItemRequestIdIn(requestIds);

        return  requests.stream()
                .map(request -> {
                            List<ItemDto> itemsDtoForRequest = itemList.stream()
                                    .filter(item -> item.getItemRequest().getId().equals(request.getId()))
                                    .map(ItemMapper::mapToItemDto)
                                    .toList();

                            return ItemRequestMapper.mapToItemRequestItemsDto(request, itemsDtoForRequest);
                        })
                .toList();
    }

    public List<ItemRequestDto> getAll(Long userId) {
        return  itemRequestStorage.findAll().stream()
                .filter(item -> !item.getRequestor().getId().equals(userId))
                .map(ItemRequestMapper::mapToItemRequestDto)
                .toList();
    }

    public ItemRequestItemsDto getRequest(Long requestId) {

        Collection<Item> items = itemStorage.searchItemsByItemRequest_Id(requestId);
        List<ItemDto> itemsDto = items.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();

        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с таким id " + requestId +  " не найден"));


        return ItemRequestMapper.mapToItemRequestItemsDto(itemRequest, itemsDto);
    }

    private User checkUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id " + userId + " не найден"));
    }

}
