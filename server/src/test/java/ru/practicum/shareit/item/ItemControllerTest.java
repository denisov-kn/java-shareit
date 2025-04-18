package ru.practicum.shareit.item;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;

import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.NewCommentRequest;
import ru.practicum.shareit.item.dto.ItemCommentDateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@DisplayName("Контроллер Item")
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Должен находить вещь по id")
    void getItemById() throws Exception {

        Long userId = 1L;
        Long itemId = 1L;

        ItemCommentDateDto itemCommentDateDto = TestData.getItemCommentDateDto(itemId);

        when(itemService.getItemById(itemId, userId)).thenReturn(itemCommentDateDto);
        mockMvc.perform(
                get("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemCommentDateDto)));
        verify(itemService, times(1)).getItemById(itemId, userId);
    }

    @Test
    @DisplayName("Должен находить все вещи пользователя")
    void getAllItemsByUserId() throws Exception  {
        Long userId = 1L;

        List<ItemCommentDateDto> itemCommentDateDtoList = TestData.getAllItemCommentDateDto();

        when(itemService.getAllItemsByUserId(userId)).thenReturn(itemCommentDateDtoList);
        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemCommentDateDtoList)));
        verify(itemService, times(1)).getAllItemsByUserId(userId);
    }

    @Test
    @DisplayName("Поиск вещей по имени и описанию")
    void searchItems()  throws Exception  {
        String text = "test";
        Long userId = 1L;

        List<ItemDto> itemDtoList = TestData.getAllItemsDto();
        when(itemService.searchItems(text, userId)).thenReturn(itemDtoList);
        mockMvc.perform(
                get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoList)));
        verify(itemService, times(1)).searchItems(text, userId);
    }

    @Test
    @DisplayName("Должен создавать вещь")
    void createItem() throws Exception {

        Long userId = 1L;
        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setDescription("test");
        newItemRequest.setName("test");
        newItemRequest.setAvailable(true);

        ItemDto itemDto = TestData.getItemDto(1L);

        when(itemService.createItem(userId, newItemRequest)).thenReturn(itemDto);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
        verify(itemService, times(1)).createItem(userId, newItemRequest);
    }

    @Test
    @DisplayName("Должен обновлять вещь")
    void updateItem() throws Exception {

        Long userId = 1L;
        Long itemId = 1L;
        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setDescription("test");
        updateItemRequest.setName("test");
        updateItemRequest.setAvailable(true);

        ItemDto itemDto = TestData.getItemDto(1L);

        when(itemService.updateItem(userId, itemId, updateItemRequest)).thenReturn(itemDto);
        mockMvc.perform(
                patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
        verify(itemService, times(1)).updateItem(userId, itemId, updateItemRequest);
    }

    @Test
    @DisplayName("Должен добавлять комментарий")
    void createComment() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("test");

        CommentDto commentDto = TestData.getCommentDto(1L);

        when(itemService.createComment(itemId, userId, newCommentRequest)).thenReturn(commentDto);
        mockMvc.perform(
                post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
        verify(itemService, times(1)).createComment(itemId, userId, newCommentRequest);
    }
}