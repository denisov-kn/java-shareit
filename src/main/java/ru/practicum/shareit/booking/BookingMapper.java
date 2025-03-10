package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor
public class BookingMapper {
    public static Booking mapToBooking(NewBookingRequest request, User user, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStartDate(request.getStart());
        booking.setEndDate(request.getEnd());
        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking, UserDto user) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItem(booking.getItem());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(user);
        return bookingDto;
    }

    public static BookingInfoDto mapToBookingInfoDto(Booking booking) {
        BookingInfoDto bookingInfoDto = new BookingInfoDto();
        bookingInfoDto.setId(booking.getId());
        bookingInfoDto.setStart(booking.getStartDate());
        bookingInfoDto.setEnd(booking.getEndDate());
        return bookingInfoDto;
    }

}
