package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
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
        return BookingDto.builder()
                .id(booking.getId())
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getStatus())
                .booker(user)
                .build();
    }

    public static BookingInfoDto mapToBookingInfoDto(Booking booking) {

        return BookingInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .build();
    }

}
