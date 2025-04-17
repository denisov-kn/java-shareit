package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.NewCommentRequest;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("Сервис Item")
class ItemServiceTest {
    private final ItemService itemService;
    private final EntityManager em;

    @Test
    @DisplayName("Получить вещь по id - getItemById")
    void getItemById() {

        Long itemId = TestData.getItemDto(1L).getId();
        Long ownerId = 3L;

        ItemCommentDateDto itemCommentDateDto = itemService.getItemById(itemId, ownerId);

        Item itemFromDB = getItemFromDB(itemId, ownerId);

        TypedQuery<Comment> queryComment = em
                .createQuery("select c from Comment c where c.itemId = :itemId", Comment.class);
        queryComment.setParameter("itemId", itemId);
        List<Comment> commentsFromDB = queryComment.getResultList();
        List<CommentDto> commentsDtoFromDB = commentsFromDB.stream()
                .map(CommentMapper::toDto)
                .toList();

        TypedQuery<Booking> queryNext = em
                .createQuery("select b from Booking b WHERE b.item.id = :itemId AND b.startDate > :currentDate" +
                " ORDER BY b.startDate DESC ", Booking.class
        );
        queryNext.setParameter("itemId", itemId);
        queryNext.setParameter("currentDate", LocalDateTime.now());
        queryNext.setMaxResults(1);
        List<Booking> resultsNext = queryNext.getResultList();
        Booking nextBookingFromDb = resultsNext.isEmpty() ? null : resultsNext.getFirst();

        BookingInfoDto nextBookingInfoDtoFromDB = nextBookingFromDb != null
                ? BookingMapper.mapToBookingInfoDto(nextBookingFromDb) : null;



        TypedQuery<Booking> queryLast = em
                .createQuery("select b from Booking b WHERE b.item.id = :itemId AND b.endDate < :currentDate" +
                " ORDER BY b.endDate DESC ", Booking.class
        );
        queryLast.setParameter("itemId", itemId);
        queryLast.setParameter("currentDate", LocalDateTime.now());
        queryNext.setMaxResults(1);
        List<Booking> resultsLast = queryLast.getResultList();
        Booking lastBookingFromDb = resultsLast.isEmpty() ? null : resultsLast.getFirst();

        BookingInfoDto lastBookingInfoDtoFromDB = lastBookingFromDb != null
                ? BookingMapper.mapToBookingInfoDto(lastBookingFromDb) : null;


        ItemCommentDateDto itemCommentDateDtoFromDB = ItemMapper.mapToItemCommentDateDto(
                itemFromDB,
                commentsDtoFromDB,
                nextBookingInfoDtoFromDB,
                lastBookingInfoDtoFromDB
        );

        assertEquals(itemCommentDateDto, itemCommentDateDtoFromDB, "Вещь с сервиса и с БД должна совпадать");

    }

    private Item getItemFromDB(Long itemId, Long ownerId) {
        TypedQuery<Item> queryItem = em
                .createQuery("select i from Item i where i.id = :itemId and i.owner.id = :ownerId", Item.class);
        queryItem.setParameter("itemId", itemId);
        queryItem.setParameter("ownerId", ownerId);
        Item itemFromDB = queryItem.getSingleResult();
        return itemFromDB;
    }

    @Test
    @DisplayName("Получить все вещи пользователя по id - getAllItemsByUserId")
    void getAllItemsByUserId() {
        Long ownerId = 3L;
        List<ItemCommentDateDto> itemCommentDateDto = itemService.getAllItemsByUserId(3L);

        TypedQuery<Item> queryItem = em
                .createQuery("select i from Item i where i.owner.id = :ownerId", Item.class);
        queryItem.setParameter("ownerId", ownerId);
        List<Item> itemFromDB = queryItem.getResultList();


        List<Long> itemIds = itemFromDB.stream().map(Item::getId).toList();

        TypedQuery<Comment> queryComment = em
                .createQuery("select c from Comment c where c.itemId IN :itemIds", Comment.class);
        queryComment.setParameter("itemIds", itemIds);
        List<Comment> commentsFromDB = queryComment.getResultList();

        Map<Long, List<CommentDto>> commentsByItemId = commentsFromDB.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        TypedQuery<Booking> queryBooking = em
                .createQuery(
                        """
SELECT b
FROM Booking b
WHERE b.item.owner.id = :ownerId
    AND (
    (b.endDate = (SELECT MAX(b1.endDate)
        FROM Booking b1
        WHERE b1.item.id = b.item.id
        AND b1.endDate < CURRENT_TIMESTAMP))
    OR
    (b.startDate = (SELECT MIN(b2.startDate)
        FROM Booking b2
        WHERE b2.item.id = b.item.id
        AND b2.startDate > CURRENT_TIMESTAMP))
    )
    ORDER BY b.item.id, b.endDate DESC, b.startDate ASC
""", Booking.class
                );
       queryBooking.setParameter("ownerId", ownerId);
       List<Booking> resultsBooking = queryBooking.getResultList();
       Map<Long, List<Booking>> lastAndNextBookings = resultsBooking.stream()
               .collect(Collectors.groupingBy(b -> b.getItem().getId()));




        List<ItemCommentDateDto> itemCommentDateDtoFromDB = itemFromDB.stream()
                .map(item -> {
                    List<Booking> bookingsItem = lastAndNextBookings.getOrDefault(item.getId(), Collections.emptyList());

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
                .toList();

        assertEquals(itemCommentDateDto, itemCommentDateDtoFromDB, "Вещь с сервиса и с БД должна совпадать");

    }

    @Test
    @DisplayName("Поиск вещи по name и description - searchItems")
    void searchItems() {
        Long userId = 1L;
        String text = "вЕщь";

        List<ItemDto> itemDtoList = itemService.searchItems(text, userId);

        TypedQuery<Item> query = em.createQuery(
                """
                    select i from Item i
                    where (lower(i.name) like lower(:text)
                    OR lower(i.description) like lower(:text))
                    and i.available = true
                    """, Item.class
        );
        query.setParameter("text", "%" + text + "%");
        List<Item> itemsFromDB = query.getResultList();
        List<ItemDto> itemsDtoFromDB = itemsFromDB.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();

        System.out.println(itemsDtoFromDB);
        System.out.println(itemDtoList);

        assertEquals(itemDtoList.size(), itemsDtoFromDB.size(),
                "Список вещей из БД и сервиса должны иметь одинаковую длину");
        assertTrue(itemDtoList.containsAll(itemsDtoFromDB),
                "Список вещей сервиса должен содержать все записи из БД");
        assertTrue(itemsDtoFromDB.containsAll(itemDtoList),
                "Список вещей из БД должен содержать все записи из сервиса");

    }

    @Test
    @DisplayName("Создать вещь - createItem")
    void createItem() {

        Long ownerId = 4L;

        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setAvailable(false);
        newItemRequest.setName("Вещь10");
        newItemRequest.setDescription("Описание Вещь10");
        ItemDto itemDto = itemService.createItem(ownerId, newItemRequest);

        Item itemFromDB = getItemFromDB(itemDto.getId(), ownerId);
        ItemDto itemDtoFromDB = ItemMapper.mapToItemDto(itemFromDB);

        assertEquals(itemDto, itemDtoFromDB, "Вещь созданная через сервис должна находиться в БД");
    }

    @Test
    @DisplayName("Обновить вещь - updateItem")
    void updateItem() {
        Long ownerId = 5L;
        Long itemId = 4L;

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setAvailable(true);
        updateItemRequest.setName("Обновленная вещь");
        updateItemRequest.setDescription("Описание обновленной вещи");

        ItemDto itemDto = itemService.updateItem(ownerId, itemId, updateItemRequest);

        Item itemFromDB = getItemFromDB(itemDto.getId(), ownerId);
        ItemDto itemDtoFromDB = ItemMapper.mapToItemDto(itemFromDB);

        assertEquals(itemDto, itemDtoFromDB, "Вещь обновленная через сервис должна находиться в БД");

    }

    @Test
    @DisplayName("Не обновлять вещь если пользователь не владелец - updateItem")
    void shouldNotUpdateItemIfUserIsNotOwner() {
        Long fakeOwnerId = 1L;
        Long realOwnerId = 5L;
        Long itemId = 4L;


        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setAvailable(true);
        updateItemRequest.setName("Обновленная вещь");
        updateItemRequest.setDescription("Описание обновленной вещи");

        assertThrows(NotFoundException.class, () -> itemService.updateItem(fakeOwnerId, itemId, updateItemRequest),
                "Сервис должен выбрасывать ошибку NotFound"
        );

        ItemDto itemDtoFromDB = ItemMapper.mapToItemDto(
                getItemFromDB(itemId, realOwnerId)
        );

        assertEquals(TestData.getItemDto(itemId),itemDtoFromDB, "Сервис не должен обновить вещь в БД");

    }


    @Test
    @DisplayName("Обновить вещь без описания и имени - updateItem")
    void shouldUpdateItemWithoutNameAndDescription() {
        Long ownerId = 5L;
        Long itemId = 4L;

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setAvailable(true);

        ItemDto itemDto = itemService.updateItem(ownerId, itemId, updateItemRequest);

        Item itemFromDB = getItemFromDB(itemDto.getId(), ownerId);
        ItemDto itemDtoFromDB = ItemMapper.mapToItemDto(itemFromDB);

        assertEquals(itemDto, itemDtoFromDB, "Вещь обновленная через сервис должна находиться в БД");

    }

    @Test
    @DisplayName("Обновить  имя вещи - updateItem")
    void shouldUpdateItemName() {
        Long ownerId = 5L;
        Long itemId = 4L;

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Обновленная вещь");

        ItemDto itemDto = itemService.updateItem(ownerId, itemId, updateItemRequest);

        Item itemFromDB = getItemFromDB(itemDto.getId(), ownerId);
        ItemDto itemDtoFromDB = ItemMapper.mapToItemDto(itemFromDB);

        assertEquals(itemDto, itemDtoFromDB, "Вещь обновленная через сервис должна находиться в БД");

    }



    @Test
    @DisplayName("Создать комментарий - createComment")
    void createComment() {
        Long bookerId = 6L;
        Long itemId = 4L;
        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Комментарий");
        CommentDto commentDto = itemService.createComment(itemId, bookerId, newCommentRequest);

        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id = :commentId", Comment.class);
        query.setParameter("commentId", commentDto.getId());
        Comment comment = query.getSingleResult();
        CommentDto commentDtoFromDB = CommentMapper.toDto(comment);

        assertEquals(commentDto, commentDtoFromDB, "Комментарий созданный с сервиса и в БД должны совпадать");

    }
}