package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.NewCommentRequest;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemCommentDateDto getItemById(@PathVariable long itemId,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
       return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemCommentDateDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.searchItems(text, userId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated @RequestBody NewItemRequest request) {
        return itemService.createItem(userId, request);
    }

    @PatchMapping("{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated @RequestBody UpdateItemRequest request) {
        return itemService.updateItem(userId, itemId, request);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Validated @RequestBody NewCommentRequest request) {
        return itemService.createComment(itemId, userId, request);
    }



}
