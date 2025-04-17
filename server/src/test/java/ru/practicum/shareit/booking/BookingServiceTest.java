package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.constants.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("Сервис Booking")
class BookingServiceTest {
    private final BookingService bookingService;
    private final EntityManager em;

    @Test
    @DisplayName("Получить бронирование по id букера - findBookingById")
    void findBookingById() {
        Long bookingId = 1L;
        Long bookerId = 2L;
        BookingDto bookingDto = bookingService.findBookingById(bookingId, bookerId);

        BookingDto bookingDtoFromDB = getBookingDtoFromDB(bookingId);

        assertEquals(bookingDto, bookingDtoFromDB,
                "Бронирование найденное в базе и через сервис должны совпадать");

    }

    @Test
    @DisplayName("Получить бронирование по id владельца - findBookingById")
    void findBookingByIdWithOwnerId() {
        Long bookingId = 1L;
        Long  ownerId = 3L;
        BookingDto bookingDto = bookingService.findBookingById(bookingId, ownerId);

        BookingDto bookingDtoFromDB = getBookingDtoFromDB(bookingId);

        assertEquals(bookingDto, bookingDtoFromDB,
                "Бронирование найденное в базе и через сервис должны совпадать");

    }

    @Test
    @DisplayName("Не должен находить бронирование если пользователь не владелец или не создавал бронирование - findBookingById")
    void shouldNotFindBookingByIdIfUserIdIsNotOwnerOrBooker() {
        Long bookingId = 1L;
        Long  userId = 5L;
        assertThrows(ForbiddenException.class, () -> bookingService.findBookingById(bookingId, userId));
    }

    @Test
    @DisplayName("Не должен получить бронирование если пользователь не найден - findBookingById")
    void shouldNotFindBookingByIdIfIdUserIsWrong() {
        Long bookingId = 1L;
        Long bookerId = 99L;
        assertThrows(NotFoundException.class, () -> bookingService.findBookingById(bookingId, bookerId));

    }

    @Test
    @DisplayName("Не должен получить бронирование если бронирование не найдено - findBookingById")
    void shouldNotFindBookingByIdIfIdBookingIsWrong() {
        Long bookingId = 99L;
        Long bookerId = 2L;
        assertThrows(NotFoundException.class, () -> bookingService.findBookingById(bookingId, bookerId));

    }



    private BookingDto getBookingDtoFromDB(Long bookingId) {
        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :bookingId", Booking.class);
        query.setParameter("bookingId", bookingId);
        Booking booking = query.getSingleResult();
        UserDto userDto = UserMapper.mapToUserDto(booking.getBooker());
        BookingDto bookingDtoFromDB = BookingMapper.mapToBookingDto(booking, userDto);
        return bookingDtoFromDB;
    }

    @Test
    @DisplayName("Одобрить бронирование - setApprove")
    void setApprove() {
        Long bookingId = 4L;
        Long ownerId = 5L;

        bookingService.setApprove(bookingId, ownerId, true);

        assertEquals(Status.APPROVED,getBookingDtoFromDB(bookingId).getStatus(),
                "Сервис должен поменять в БД статус бронирования на APPROVED");
    }

    @Test
    @DisplayName("Отклонить бронирование - setApprove")
    void setApproveRejected() {
        Long bookingId = 4L;
        Long ownerId = 5L;

        bookingService.setApprove(bookingId, ownerId, false);

        assertEquals(Status.REJECTED,getBookingDtoFromDB(bookingId).getStatus(),
                "Сервис должен поменять в БД статус бронирования на REJECTED");
    }

    @Test
    @DisplayName("Создать бронирование - createBooking")
    void createBooking() {

        Long itemId = 2L;
        Long bookerId = 6L;

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setStart(LocalDateTime.now().plusDays(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusDays(5));
        newBookingRequest.setItemId(itemId);

        BookingDto bookingDto = bookingService.createBooking(newBookingRequest, bookerId);

        BookingDto bookingDtoFromDb = getBookingDtoFromDB(bookingDto.getId());

        assertEquals(bookingDto, bookingDtoFromDb,
                "Сервис должен создавать бронирование и такое бронирование должно быть в БД");

    }


    @Test
    @DisplayName("Не должен создавать бронирование если вещь недоступна - createBooking")
    void shouldNotCreateBookingIfItemNotAvailable() {

        Long itemId = 3L;
        Long bookerId = 6L;

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setStart(LocalDateTime.now().plusDays(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusDays(5));
        newBookingRequest.setItemId(itemId);

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(newBookingRequest, bookerId));
    }

    @Test
    @DisplayName("Не должен создавать бронирование если вещь не найдена- createBooking")
    void shouldNotCreateBookingIfItemNotFound() {

        Long itemId = 10L;
        Long bookerId = 6L;

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setStart(LocalDateTime.now().plusDays(1));
        newBookingRequest.setEnd(LocalDateTime.now().plusDays(5));
        newBookingRequest.setItemId(itemId);

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(newBookingRequest, bookerId));
    }

    @Test
    @DisplayName("Получить все бронирования для пользователя - findAllBookingsByUserIdAndState")
    void findAllBookingsByUserIdAndState() {
        Long bookerId = 6L;


        List<BookingDto> bookingDtoList = bookingService.findAllBookingsByUserIdAndState(bookerId, "ALL");

        TypedQuery<Booking> query =
                em.createQuery("select b from Booking b where b.booker.id = :bookerId", Booking.class);
        query.setParameter("bookerId", bookerId);
        List<Booking> bookings = query.getResultList();
        List<BookingDto> bookingDtoFromDB = bookings.stream()
                .map(booking -> {
                    UserDto userDto = UserMapper.mapToUserDto(booking.getBooker());
                    return BookingMapper.mapToBookingDto(booking, userDto);
                })
                .toList();

        assertEquals(bookingDtoList.size(), bookingDtoFromDB.size(),
                "Список бронирований из БД и сервиса должны иметь одинаковую длину");
        assertTrue(bookingDtoList.containsAll(bookingDtoFromDB),
                "Список бронирований сервиса должен содержать все записи из БД");
        assertTrue(bookingDtoFromDB.containsAll(bookingDtoList),
                "Список бронирований из БД должен содержать все записи из сервиса");
    }

    @Test
    @DisplayName("Получить все бронирования для владельца вещи - findAllBookingsByOwnerIdAndState")
    void findAllBookingsByOwnerIdAndState() {
        Long ownerId = 3L;
        List<BookingDto> bookingDtoList = bookingService.findAllBookingsByOwnerIdAndState(ownerId, "ALL");

        TypedQuery<Booking> query =
                em.createQuery("select b from Booking b where b.item.owner.id = :ownerId", Booking.class);
        query.setParameter("ownerId", ownerId);
        List<Booking> bookings = query.getResultList();
        List<BookingDto> bookingDtoFromDB = bookings.stream()
                .map(booking -> {
                    UserDto userDto = UserMapper.mapToUserDto(booking.getBooker());
                    return BookingMapper.mapToBookingDto(booking, userDto);
                })
                .toList();

        assertEquals(bookingDtoList.size(), bookingDtoFromDB.size(),
                "Список бронирований из БД и сервиса должны иметь одинаковую длину");
        assertTrue(bookingDtoList.containsAll(bookingDtoFromDB),
                "Список бронирований сервиса должен содержать все записи из БД");
        assertTrue(bookingDtoFromDB.containsAll(bookingDtoList),
                "Список бронирований из БД должен содержать все записи из сервиса");


    }



    @Test
    @DisplayName("Получить все бронирования для пользователя в статусе APPROVED - findAllBookingsByUserIdAndState")
    void findAllBookingsByUserIdAndStateApproved() {
        Long bookerId = 6L;
        Status status = Status.APPROVED;


        List<BookingDto> bookingDtoList = bookingService.findAllBookingsByUserIdAndState(bookerId, status.toString());

        TypedQuery<Booking> query =
                em.createQuery("select b from Booking b where b.booker.id = :bookerId and b.status = :status", Booking.class);
        query.setParameter("bookerId", bookerId);
        query.setParameter("status", status);
        List<Booking> bookings = query.getResultList();
        List<BookingDto> bookingDtoFromDB = bookings.stream()
                .map(booking -> {
                    UserDto userDto = UserMapper.mapToUserDto(booking.getBooker());
                    return BookingMapper.mapToBookingDto(booking, userDto);
                })
                .toList();

        assertEquals(bookingDtoList.size(), bookingDtoFromDB.size(),
                "Список бронирований из БД и сервиса должны иметь одинаковую длину");
        assertTrue(bookingDtoList.containsAll(bookingDtoFromDB),
                "Список бронирований сервиса должен содержать все записи из БД");
        assertTrue(bookingDtoFromDB.containsAll(bookingDtoList),
                "Список бронирований из БД должен содержать все записи из сервиса");
    }

    @Test
    @DisplayName("Получить все бронирования для владельца вещи в статусе WAITING- findAllBookingsByOwnerIdAndState")
    void findAllBookingsByOwnerIdAndStateWaiting() {
        Long ownerId = 3L;
        Status status = Status.WAITING;
        List<BookingDto> bookingDtoList = bookingService.findAllBookingsByOwnerIdAndState(
                ownerId,
                status.toString()
        );

        TypedQuery<Booking> query =
                em.createQuery("select b from Booking b where b.item.owner.id = :ownerId and b.status = :status", Booking.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("status", status);
        List<Booking> bookings = query.getResultList();
        List<BookingDto> bookingDtoFromDB = bookings.stream()
                .map(booking -> {
                    UserDto userDto = UserMapper.mapToUserDto(booking.getBooker());
                    return BookingMapper.mapToBookingDto(booking, userDto);
                })
                .toList();

        assertEquals(bookingDtoList.size(), bookingDtoFromDB.size(),
                "Список бронирований из БД и сервиса должны иметь одинаковую длину");
        assertTrue(bookingDtoList.containsAll(bookingDtoFromDB),
                "Список бронирований сервиса должен содержать все записи из БД");
        assertTrue(bookingDtoFromDB.containsAll(bookingDtoList),
                "Список бронирований из БД должен содержать все записи из сервиса");
    }

    @Test
    @DisplayName("Не должен создавать бронирование если дата начала бронирование позже даты окончания")
    void shouldNotCreateBookingWithWrongDate() {

        Long itemId = 2L;
        Long bookerId = 6L;

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setStart(LocalDateTime.now().plusDays(7));
        newBookingRequest.setEnd(LocalDateTime.now().plusDays(1));
        newBookingRequest.setItemId(itemId);

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(newBookingRequest, bookerId));
    }

    @Test
    @DisplayName("Не должен одобрять бронирование если пользователь не владелец - setApprove")
    void shouldNotSetApproveWithWrongOwnerId() {
        Long bookingId = 4L;
        Long ownerId = 4L;

        assertThrows(ForbiddenException.class, () -> bookingService.setApprove(bookingId, ownerId, true));

    }




}