package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@DisplayName("Контроллер Booking")
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Должен возвращать бронирование")
    void getBooking() throws Exception {
        long bookingId = 1L;
        long userId = 1L;
        BookingDto bookingDto = TestData.getBookingDto(bookingId);

        when(bookingService.findBookingById(bookingId, userId)).thenReturn(bookingDto);
        mockMvc.perform(
                        get("/bookings/{id}", bookingId)
                                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto))
                );
        verify(bookingService, times(1)).findBookingById(bookingId, userId);
    }

    @Test
    @DisplayName("Должен отклонять согласование для бронирования")
    void setApprove() throws Exception {

        long bookingId = 1L;
        long userId = 1L;
        BookingDto bookingDto = TestData.getBookingDto(bookingId);
        bookingDto.setStatus(Status.REJECTED);
        when(bookingService.setApprove(bookingId, userId, false)).thenReturn(bookingDto);
        mockMvc.perform(
                patch("/bookings/{id}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
        verify(bookingService, times(1)).setApprove(bookingId, userId, false);
    }

    @Test
    @DisplayName("Должен создавать бронирование")
    void createBooking() throws Exception {

        long userId = 1L;
        long bookingId = 1L;

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(1L);
        newBookingRequest.setEnd(LocalDateTime.now().plusDays(8));
        newBookingRequest.setStart(LocalDateTime.now().plusDays(2));

        BookingDto bookingDto = TestData.getBookingDto(bookingId);
        when(bookingService.createBooking(newBookingRequest,userId )).thenReturn(bookingDto);
        mockMvc.perform(
                post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto))
                );
        verify(bookingService, times(1)).createBooking(newBookingRequest,userId);
    }

    @Test @DisplayName("Получить все бронирования для пользователя ")
    void getBookingByUserIdAndState() throws Exception {

        long userId = 1L;
        List<BookingDto> bookingDtoList = TestData.getAllBookingsDto();
        when(bookingService.findAllBookingsByUserIdAndState(userId, "ALL")).thenReturn(bookingDtoList);
        mockMvc.perform(
                get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDtoList))
        );
        verify(bookingService, times(1)).findAllBookingsByUserIdAndState(userId, "ALL");
    }


    @Test
    @DisplayName("Получить все бронирования для владельца")
    void getBookingByOwnerIdAndState() throws Exception {

        long userId = 1L;
        List<BookingDto> bookingDtoList = TestData.getAllBookingsDto();
        when(bookingService.findAllBookingsByOwnerIdAndState(userId, "ALL")).thenReturn(bookingDtoList);
        mockMvc.perform(
                        get("/bookings/owner")
                                .param("state", "ALL")
                                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDtoList))
                );
        verify(bookingService, times(1)).findAllBookingsByOwnerIdAndState(userId, "ALL");
    }


}