package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("Сервис ItemRequest")
class ItemRequestServiceTest {

    private final ItemRequestService itemRequestService;
    private final EntityManager em;


    private ItemRequestDto getItemRequestDtoFromDB(Long  itemRequestId) {
        TypedQuery<ItemRequest> query = em.createQuery("select i from ItemRequest i where i.id = :itemRequestId", ItemRequest.class);
        query.setParameter("itemRequestId", itemRequestId);
        ItemRequest itemRequest = query.getSingleResult();
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }


    @Test
    @DisplayName("Создать запрос вещи  - create")
    void create() {

        NewItemRequestDto newItemRequestDto = new NewItemRequestDto();
        newItemRequestDto.setDescription("ЗапросВещи5");
        Long userId = TestData.getTestUserDto(3L).getId();
        ItemRequestDto itemRequestDto = itemRequestService.create(newItemRequestDto,
                userId);
        assertEquals(itemRequestDto, getItemRequestDtoFromDB(itemRequestDto.getId()));

    }

    @Test
    @DisplayName("Получить все запросы одного пользователя  - getAllForUser")
    void getAllForUser() {

        Long userId = 2L;

        List<ItemRequestItemsDto> itemRequestsDto = itemRequestService.getAllForUser(userId);

        TypedQuery<ItemRequest> queryRequest = em.createQuery("select i from ItemRequest i where i.requestor.id = :requestorId", ItemRequest.class);
        queryRequest.setParameter("requestorId", userId);
        List<ItemRequest> itemRequestsFromDb = queryRequest.getResultList();

        List<Long> requestIds = itemRequestsFromDb.stream()
                .map(ItemRequest::getId)
                .toList();

        TypedQuery<Item> queryItem = em.createQuery("select i from Item i where i.itemRequest.id in :itemRequestIds", Item.class);
        queryItem.setParameter("itemRequestIds", requestIds);
        List<Item> itemsFromDb = queryItem.getResultList();


        List<ItemRequestItemsDto> itemRequestsDtoFromDB = itemRequestsFromDb.stream()
                .map(itemRequest -> {
                    List<ItemDto> itemsDtoForRequest = itemsFromDb.stream()
                            .filter(item -> item.getItemRequest().getId().equals(itemRequest.getId()))
                            .map(ItemMapper::mapToItemDto)
                            .toList();
                    return  ItemRequestMapper.mapToItemRequestItemsDto(
                            itemRequest,
                            itemsDtoForRequest
                    );
                })
                .toList();

        assertEquals(itemRequestsDto.size(), itemRequestsDtoFromDB.size(),
                "Список запросов  из БД и сервиса должны иметь одинаковую длину");
        assertTrue(itemRequestsDto.containsAll(itemRequestsDtoFromDB),
                "Список запросов сервиса должен содержать все записи из БД");
        assertTrue(itemRequestsDtoFromDB.containsAll(itemRequestsDto),
                "Список запросов  из БД должен содержать все записи из сервиса");

    }

    @Test
    @DisplayName("Получить все запросы - getAll")
    void getAll() {


        Long userId = 1L;

        List<ItemRequestDto> itemRequestsDto = itemRequestService.getAll(userId);

        TypedQuery<ItemRequest> queryRequest = em.createQuery("select i from ItemRequest i where i.requestor.id != :userId ", ItemRequest.class);
        queryRequest.setParameter("userId", userId);
        List<ItemRequest> itemRequestsFromDb = queryRequest.getResultList();
        List<ItemRequestDto> itemRequestsDtoFromDB = itemRequestsFromDb.stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .toList();

        assertEquals(itemRequestsDto.size(), itemRequestsDtoFromDB.size(),
                "Список запросов  из БД и сервиса должны иметь одинаковую длину");
        assertTrue(itemRequestsDto.containsAll(itemRequestsDtoFromDB),
                "Список запросов сервиса должен содержать все записи из БД");
        assertTrue(itemRequestsDtoFromDB.containsAll(itemRequestsDto),
                "Список запросов  из БД должен содержать все записи из сервиса");
    }

    @Test
    @DisplayName("Получить все запрос с вещами - getRequest")
    void getRequest() {

        ItemRequestItemsDto itemRequestItemsDto = itemRequestService.getRequest(3L);

        TypedQuery<ItemRequest> queryRequest = em.createQuery("select i from ItemRequest i where i.id = :itemRequestId", ItemRequest.class);
        queryRequest.setParameter("itemRequestId", 3L);
        ItemRequest itemRequestFromDb = queryRequest.getSingleResult();

        TypedQuery<Item> queryItem = em.createQuery("select i from Item i where i.itemRequest.id = :itemRequestId", Item.class);
        queryItem.setParameter("itemRequestId", 3L);
        List<ItemDto> itemsDto = queryItem.getResultList().stream()
                .map(ItemMapper::mapToItemDto)
                .toList();

        ItemRequestItemsDto itemRequestItemsDtoFromDB = ItemRequestMapper
                .mapToItemRequestItemsDto(itemRequestFromDb, itemsDto);

        assertEquals(itemRequestItemsDto, itemRequestItemsDtoFromDB,
                "Запрос с вещами с сервиса  должен совпадать с запросом с вещами из БД");

    }
}