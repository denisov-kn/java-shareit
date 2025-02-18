package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

@NoArgsConstructor
public class BookingMapper {
    public static Booking mapToBooking(NewBookingRequest request) {
        Booking booking = new Booking();
        booking.setItem(request.getItem());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItem(booking.getItem());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

}
