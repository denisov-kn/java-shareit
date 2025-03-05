package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

    public static BookingDto mapToBookingDto(Booking booking, User user) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItem(booking.getItem());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(user);
        return bookingDto;
    }

}
