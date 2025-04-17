package ru.practicum.shareit.booking;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingClient.getBooking(userId, bookingId);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApprove(@PathVariable long bookingId,
                                 @RequestParam boolean approved,
                                 @RequestHeader("X-Sharer-User-Id") long ownerId) {

        return bookingClient.setApprove(ownerId, bookingId, approved);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid NewBookingRequest newBookingRequest,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return  bookingClient.createBooking(userId, newBookingRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUserIdAndState(@RequestParam (defaultValue = "ALL") String stateParam,
                                                             @RequestHeader("X-Sharer-User-Id") long userId) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.getBookingByUserIdAndState(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwnerIdAndState(@RequestParam (defaultValue = "ALL") String stateParam,
                                                              @RequestHeader("X-Sharer-User-Id") long ownerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.getBookingByOwnerIdAndState(ownerId, state);
    }


}
