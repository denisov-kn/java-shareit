package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.constants.Status;

import java.util.Collection;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    Collection<Booking> findBookingsByBookerIdAndStatus(Long booker_id, Status status);
    Collection<Booking> findBookingsByBookerId(Long booker_id);

    Collection<Booking> findBookingsByItem_Owner_Id(Long itemOwnerId);
    Collection<Booking> findBookingsByItem_Owner_IdAndStatus(Long itemOwnerId, Status status);
}
