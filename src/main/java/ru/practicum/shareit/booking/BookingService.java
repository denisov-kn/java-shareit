package ru.practicum.shareit.booking;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;

    public BookingDto findBookingById(Long bookingId, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.mapToBookingDto(booking,  UserMapper.mapToUserDto(user));
        } else throw new ForbiddenException("У пользователя нет доступа к бронированию");
    }

    @Transactional
    public BookingDto setApprove(Long bookingId, Long ownerId, Boolean approve) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!booking.getItem().getOwner().getId().equals(ownerId))
             throw new ForbiddenException("Пользователь не является владельцем бронирования");
        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else booking.setStatus(Status.REJECTED);

        bookingStorage.save(booking);
        return BookingMapper.mapToBookingDto(booking, UserMapper.mapToUserDto(booking.getBooker()));
    }

    public BookingDto createBooking(NewBookingRequest newBookingRequest, Long userId) {

        User booker = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemStorage.findById(newBookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (newBookingRequest.getEnd().isEqual(newBookingRequest.getStart()))
            throw new BadRequestException("Время начала бронирования (" +
                    newBookingRequest.getStart() + ") "
                    + "не может быть равным времени окончания ("
                    + newBookingRequest.getEnd() + ")");
        if (newBookingRequest.getEnd().isBefore(newBookingRequest.getStart()))
            throw new BadRequestException("Время начала бронирования (" +
                    newBookingRequest.getStart() + ") "
                    + "не может быть позже времени окончания ("
                    + newBookingRequest.getEnd() + ")");

        if (!item.getAvailable())
            throw new BadRequestException("Item " + item.getId() + " недоступен для бронирования");

        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStartDate(newBookingRequest.getStart());
        booking.setEndDate(newBookingRequest.getEnd());
        booking = bookingStorage.save(booking);

        return  BookingMapper.mapToBookingDto(booking, UserMapper.mapToUserDto(booker));
    }

    public Collection<BookingDto> findAllBookingsByUserIdAndState(Long userId, String state) {

        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (state.equals("ALL"))
            return bookingStorage.findBookingsByBookerId(userId).stream()
                    .map(booking -> BookingMapper.mapToBookingDto(
                            booking,
                            UserMapper.mapToUserDto(booking.getBooker()))
                    )
                    .collect(Collectors.toList());
        else {
            Status status = Status.valueOf(state);
            return bookingStorage.findBookingsByBookerIdAndStatus(userId, status).stream()
                    .map(booking -> BookingMapper.mapToBookingDto(
                            booking,
                            UserMapper.mapToUserDto(booking.getBooker()))
                    )
                    .collect(Collectors.toList());
        }

    }

    public Collection<BookingDto> findAllBookingsByOwnerIdAndState(Long ownerId, String state) {

        userStorage.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found"));
        if (state.equals("ALL"))
            return bookingStorage.findBookingsByItem_Owner_Id(ownerId).stream()
                    .map(booking -> BookingMapper.mapToBookingDto(
                            booking,
                            UserMapper.mapToUserDto(booking.getBooker()))
                    )
                    .collect(Collectors.toList());
        else {
           Status status = Status.valueOf(state);
           return bookingStorage.findBookingsByItem_Owner_IdAndStatus(ownerId, status).stream()
                   .map(booking -> BookingMapper.mapToBookingDto(
                           booking,
                           UserMapper.mapToUserDto(booking.getBooker()))
                   )
                   .collect(Collectors.toList());
        }
    }
}
