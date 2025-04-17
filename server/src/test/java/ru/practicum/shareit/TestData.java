package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestData {

    //USERS

    private static final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("User1")
            .email("1@mail.ru")
            .build();

    private static final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .name("User2")
            .email("2@mail.ru")
            .build();

    private static final UserDto userDto3 = UserDto.builder()
            .id(3L)
            .name("User3")
            .email("3@mail.ru")
            .build();

    private static final Map<Long, UserDto> users = Map.of(
            userDto1.getId(), userDto1,
            userDto2.getId(), userDto2,
            userDto3.getId(), userDto3
    );


    public static UserDto getTestUserDto(Long id) {
        return users.get(id);
    }

    //ITEM_REQUESTS

    private static final ItemRequestDto requestDto1 = ItemRequestDto.builder()
            .id(1L)
            .description("ЗапросВещь1")
            .created(LocalDateTime.parse("2025-04-10T10:30:00"))
            .build();

    private static final ItemRequestDto requestDto2 = ItemRequestDto.builder()
            .id(2L)
            .description("ЗапросВещь2")
            .created(LocalDateTime.parse("2025-04-10T10:30:00"))
            .build();

    private static final ItemRequestDto requestDto3 = ItemRequestDto.builder()
            .id(3L)
            .description("ЗапросВещь3")
            .created(LocalDateTime.parse("2025-04-10T10:30:00"))
            .build();

    private static final Map<Long, ItemRequestDto> itemsRequest = Map.of(
            requestDto1.getId(),requestDto1,
            requestDto2.getId(), requestDto2,
            requestDto3.getId(), requestDto3
    );

    public static ItemRequestDto getItemRequestDto(Long id) {
        return itemsRequest.get(id);
    }

    public static List<ItemRequestDto> getALLItemRequestDto() {
        return new ArrayList<>(itemsRequest.values());

    }



    //ITEMS

    private static final ItemDto itemDto1 = ItemDto.builder()
            .id(1L)
            .name("Вещь1")
            .description("Описание вещь1")
            .available(true)
            .build();

    private static final ItemDto itemDto2 = ItemDto.builder()
            .id(2L)
            .name("Вещь2")
            .description("Описание вещь2")
            .available(true)
            .build();

    private static final ItemDto itemDto3 = ItemDto.builder()
            .id(3L)
            .name("Вещь3")
            .description("Описание вещь3")
            .available(false)
            .build();

    private static final ItemDto itemDto4 = ItemDto.builder()
            .id(4L)
            .name("Вещь4")
            .description("Описание вещь4")
            .available(false)
            .build();


    private static final Map<Long, ItemDto> items = Map.of(
            itemDto1.getId(), itemDto1,
            itemDto2.getId(), itemDto2,
            itemDto3.getId(), itemDto3,
            itemDto4.getId(), itemDto4
    );

    public static ItemDto getItemDto(Long id) {
        return items.get(id);
    }

    public static List<ItemDto> getAllItemsDto() {
        return new ArrayList<>(items.values());
    }

    //BOOKING

    private static final BookingDto bookingDto1 = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.parse("2025-04-10T10:30:00"))
            .end(LocalDateTime.parse("2025-04-12T10:30:00"))
            .status(Status.APPROVED)
            .booker(getTestUserDto(2L))
            .item(getItemDto(1L))
            .build();

    private static final BookingDto bookingDto2 = BookingDto.builder()
            .id(2L)
            .start(LocalDateTime.parse("2025-03-10T10:30:00"))
            .end(LocalDateTime.parse("2025-03-12T10:30:00"))
            .status(Status.APPROVED)
            .booker(getTestUserDto(4L))
            .item(getItemDto(1L))
            .build();

    private static final BookingDto bookingDto3 = BookingDto.builder()
            .id(3L)
            .start(LocalDateTime.parse("2025-04-10T10:30:00"))
            .end(LocalDateTime.parse("2025-04-12T10:30:00"))
            .status(Status.APPROVED)
            .booker(getTestUserDto(6L))
            .item(getItemDto(4L))
            .build();

    private static final BookingDto bookingDto4 = BookingDto.builder()
            .id(4L)
            .start(LocalDateTime.parse("2025-04-10T10:30:00"))
            .end(LocalDateTime.parse("2025-04-12T10:30:00"))
            .status(Status.WAITING)
            .booker(getTestUserDto(6L))
            .item(getItemDto(4L))
            .build();

    private static final Map<Long, BookingDto> bookings = Map.of(
            bookingDto1.getId(), bookingDto1,
            bookingDto2.getId(), bookingDto2,
            bookingDto3.getId(), bookingDto3,
            bookingDto4.getId(), bookingDto4
    );

    public static BookingDto getBookingDto(Long id) {
        return bookings.get(id);
    }

    public static List<BookingDto> getAllBookingsDto() {
        return new ArrayList<>(bookings.values());
    }

    //ItemCommentDateDto

    private static final ItemCommentDateDto itemCommentDateDto1 = ItemCommentDateDto.builder()
            .id(1L)
            .comments(List.of(
                    CommentDto.builder()
                            .id(11L)
                            .itemId(12L)
                            .created(LocalDateTime.now())
                            .text("Комментарий 1")
                            .authorName("Автор1")
                            .build(),

                    CommentDto.builder()
                            .id(12L)
                            .itemId(13L)
                            .created(LocalDateTime.now())
                            .text("Комментарий 2")
                            .authorName("Автор2")
                            .build()
            ))
            .nextBooking(
                    BookingInfoDto.builder()
                            .id(13L)
                            .start(LocalDateTime.now())
                            .end(LocalDateTime.now())
                            .build()
            )
            .lastBooking(
                    BookingInfoDto.builder()
                            .id(14L)
                            .start(LocalDateTime.now())
                            .end(LocalDateTime.now())
                            .build()
            )
            .available(true)
            .description("Описание 10")
            .name("Вещь10")
            .build();

    private static final ItemCommentDateDto itemCommentDateDto2 = ItemCommentDateDto.builder()
            .id(2L)
            .comments(List.of(
                    CommentDto.builder()
                            .id(21L)
                            .itemId(22L)
                            .created(LocalDateTime.now())
                            .text("Комментарий 1")
                            .authorName("Автор1")
                            .build(),

                    CommentDto.builder()
                            .id(23L)
                            .itemId(24L)
                            .created(LocalDateTime.now())
                            .text("Комментарий 2")
                            .authorName("Автор2")
                            .build()
            ))
            .nextBooking(
                    BookingInfoDto.builder()
                            .id(25L)
                            .start(LocalDateTime.now())
                            .end(LocalDateTime.now())
                            .build()
            )
            .lastBooking(
                    BookingInfoDto.builder()
                            .id(26L)
                            .start(LocalDateTime.now())
                            .end(LocalDateTime.now())
                            .build()
            )
            .available(true)
            .description("Описание 20")
            .name("Вещь20")
            .build();

    private static final Map<Long, ItemCommentDateDto>  itemCommentDateDtoList = Map.of(
            itemCommentDateDto1.getId(),itemCommentDateDto1,
            itemCommentDateDto2.getId(),itemCommentDateDto2
    );

    public static ItemCommentDateDto getItemCommentDateDto(Long id) {
        return itemCommentDateDtoList.get(id);
    }

    public static List<ItemCommentDateDto> getAllItemCommentDateDto() {
        return new ArrayList<>(itemCommentDateDtoList.values());
    }


    //ItemRequestItem

    private static final ItemRequestItemsDto itemRequestItemsDto1 = ItemRequestItemsDto.builder()
            .id(1L)
            .created(LocalDateTime.now())
            .description("Описание запроса")
            .items(List.of(
                    ItemDto.builder()
                            .id(1L)
                            .available(true)
                            .description("Описание Вещь10")
                            .name("Вещь10")
                            .build(),
                    ItemDto.builder()
                            .id(2L)
                            .available(true)
                            .description("Описание Вещь11")
                            .name("Вещь11")
                            .build()
                    )
            )
            .build();

    private static final Map<Long, ItemRequestItemsDto> itemRequestItemsDtoList = Map.of(
            itemRequestItemsDto1.getId(), itemRequestItemsDto1
    );

    public static ItemRequestItemsDto getItemRequestItemsDto(Long id) {
        return itemRequestItemsDtoList.get(id);
    }

    //Comment
    private static final CommentDto commentDto1 = CommentDto.builder()
            .id(1L)
            .itemId(2L)
            .created(LocalDateTime.now())
            .text("Комментарий 1")
            .authorName("Автор2")
            .build();

    private static final CommentDto commentDto2 = CommentDto.builder()
            .id(2L)
            .itemId(3L)
            .created(LocalDateTime.now())
            .text("Комментарий 3")
            .authorName("Автор3")
            .build();

    private static final Map<Long, CommentDto> commentDtoList = Map.of(
            commentDto1.getId(), commentDto1,
            commentDto2.getId(), commentDto2
    );

    public static CommentDto getCommentDto(Long id) {
        return commentDtoList.get(id);
    }

    public static List<CommentDto> getAllCommentDto() {
        return new ArrayList<>(commentDtoList.values());
    }












}
