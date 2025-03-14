package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody NewItemRequestDto newItemRequestDto) {
        return itemRequestService.create(newItemRequestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getAllForUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllForUser(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getAll() {
        return itemRequestService.getAll();
    }

    @GetMapping("{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestItemsDto getRequest(@PathVariable long requestId) {
        return itemRequestService.getRequest(requestId);
    }

}
