package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comments.*;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    public final ItemStorage itemStorage;
    public final UserStorage userStorage;
    public final CommentStorage commentStorage;
    public final BookingStorage bookingStorage;

    public ItemCommentDateDto getItemById(Long itemId, Long userId) {

        checkUser(userId);

        Collection<CommentDto> commentsDto = commentStorage.searchCommentsByItemId(itemId).stream()
                .map(CommentMapper::toDto)
                .toList();

        Item item = checkItem(itemId);

        Booking lastBooking = null;
        Booking nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingStorage.findLastBookingByItemId(itemId);
            nextBooking = bookingStorage.findNextBookingByItemId(itemId);
        }

        BookingInfoDto lastBookingDto = (lastBooking != null) ? BookingMapper.mapToBookingInfoDto(lastBooking) : null;
        BookingInfoDto nextBookingDto = (nextBooking != null) ? BookingMapper.mapToBookingInfoDto(nextBooking) : null;

        return ItemMapper.mapToItemCommentDateDto(item, commentsDto, nextBookingDto, lastBookingDto);
    }



    public Collection<ItemCommentDateDto> getAllItemsByUserId(Long userId) {
        checkUser(userId);
        Collection<Item> items = itemStorage.getAllItemsByOwnerId(userId);

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();


        Map<Long, List<CommentDto>> commentsByItemId = commentStorage.searchCommentByItemIdIn(itemIds)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId));


        Map<Long, List<Booking>> bookingsByItem = bookingStorage.findLastAndNextBookingsByOwnerId(userId)
                .stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        return items.stream()
                .map(item -> {
                    List<Booking> bookingsItem = bookingsByItem.getOrDefault(item.getId(), Collections.emptyList());

                    BookingInfoDto lastBooking = !bookingsItem.isEmpty()
                            ? BookingMapper.mapToBookingInfoDto(bookingsItem.get(0))
                            : null;

                    BookingInfoDto nextBooking = bookingsItem.size() > 1
                            ? BookingMapper.mapToBookingInfoDto(bookingsItem.get(1))
                            : null;

                    return ItemMapper.mapToItemCommentDateDto(
                            item,
                            commentsByItemId.getOrDefault(item.getId(), Collections.emptyList()),
                            nextBooking,
                            lastBooking
                    );
                })
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
        User user = checkUser(userId);
        Item item = ItemMapper.mapToItem(request, user);
        item = itemStorage.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request) {
        checkUser(userId);
        if (!itemStorage.existsByIdAndOwnerId(itemId, userId))
            throw new NotFoundException("Вещь с Id: " + itemId + " не принадлежит пользователю с Id: " + userId);

        Item item = checkItem(itemId);

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }
        itemStorage.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    public CommentDto createComment(Long itemId, Long userId, NewCommentRequest request) {

        User booker = checkUser(userId);
        checkItem(itemId);
        Booking booking = bookingStorage.findBookingsByBookerIdAndStatus(userId, Status.APPROVED).stream()
                .findFirst().orElseThrow(() -> new NotFoundException("Booking not found"));

        if (booking.getEndDate().isAfter(LocalDateTime.now()))
            throw new BadRequestException("Нельзя оставить комментарий, пока не закончилось бронирование");

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setItemId(itemId);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());

        commentStorage.save(comment);
        return CommentMapper.toDto(comment);
    }

    private User checkUser(Long userId) {
        return  userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id: " + userId + " не найден"));
    }

    private Item checkItem(Long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item с id: " + itemId + " не найден"));
    }

}
