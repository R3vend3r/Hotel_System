package hotel_system.service.entity;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dao.RoomBookingDAO;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.Room;
import hotel_system.model.entity.RoomBooking;
import hotel_system.model.mapper.RoomBookingMapper;
import hotel_system.service.entityService.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private RoomBookingDAO roomBookingDAO;

    @Mock
    private RoomBookingMapper roomBookingMapper;

    @InjectMocks
    private BookingService bookingService;

    // ==================== findActiveBookingEntityByRoom() TESTS ====================

    @Test
    void findActiveBookingEntityByRoom_shouldReturnBookingWhenFound() {
        Integer roomNumber = 101;
        Client client = new Client("John", "Doe");
        client.setId("CL-456");
        Room room = new Room();
        room.setNumber(roomNumber);
        RoomBooking booking = new RoomBooking(client, room, 150.0, new Date(), new Date());

        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.of(booking));

        Optional<RoomBooking> result = bookingService.findActiveBookingEntityByRoom(roomNumber);

        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
    }

    @Test
    void findActiveBookingEntityByRoom_shouldReturnEmptyWhenNotFound() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.empty());

        Optional<RoomBooking> result = bookingService.findActiveBookingEntityByRoom(roomNumber);

        assertFalse(result.isPresent());
        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
    }

    @Test
    void findActiveBookingEntityByRoom_shouldThrowServiceExceptionWhenDaoFails() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> bookingService.findActiveBookingEntityByRoom(roomNumber));

        assertTrue(exception.getMessage().contains("Failed to find active booking by room: " + roomNumber));
        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
    }

    // ==================== findActiveBookingEntityByClientId() TESTS ====================

    @Test
    void findActiveBookingEntityByClientId_shouldReturnBookingWhenFound() {
        String clientId = "CL-123";
        Client client = new Client("John", "Doe");
        client.setId(clientId);
        Room room = new Room();
        room.setNumber(101);
        RoomBooking booking = new RoomBooking(client, room, 150.0, new Date(), new Date());

        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.of(booking));

        Optional<RoomBooking> result = bookingService.findActiveBookingEntityByClientId(clientId);

        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
    }

    @Test
    void findActiveBookingEntityByClientId_shouldReturnEmptyWhenNotFound() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.empty());

        Optional<RoomBooking> result = bookingService.findActiveBookingEntityByClientId(clientId);

        assertFalse(result.isPresent());
        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
    }

    @Test
    void findActiveBookingEntityByClientId_shouldThrowServiceExceptionWhenDaoFails() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> bookingService.findActiveBookingEntityByClientId(clientId));

        assertTrue(exception.getMessage().contains("Failed to find active booking by client: " + clientId));
        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
    }

    // ==================== findClientByRoom() TESTS ====================

    @Test
    void findClientByRoom_shouldReturnClientWhenFound() {
        Integer roomNumber = 101;
        Client client = new Client("John", "Doe");
        client.setId("CL-456");
        Room room = new Room();
        room.setNumber(roomNumber);
        RoomBooking booking = new RoomBooking(client, room, 150.0, new Date(), new Date());

        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.of(booking));

        Optional<Client> result = bookingService.findClientByRoom(roomNumber);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
    }

    @Test
    void findClientByRoom_shouldReturnEmptyWhenNoActiveBooking() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.empty());

        Optional<Client> result = bookingService.findClientByRoom(roomNumber);

        assertFalse(result.isPresent());
        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
    }

    @Test
    void findClientByRoom_shouldPropagateException() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class,
                () -> bookingService.findClientByRoom(roomNumber));
    }

    // ==================== findRoomByClientId() TESTS ====================

    @Test
    void findRoomByClientId_shouldReturnRoomNumberWhenFound() {
        String clientId = "CL-123";
        Integer expectedRoomNumber = 101;
        Client client = new Client("John", "Doe");
        client.setId(clientId);
        Room room = new Room();
        room.setNumber(expectedRoomNumber);
        RoomBooking booking = new RoomBooking(client, room, 150.0, new Date(), new Date());

        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.of(booking));

        Optional<Integer> result = bookingService.findRoomByClientId(clientId);

        assertTrue(result.isPresent());
        assertEquals(expectedRoomNumber, result.get());
        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
    }

    @Test
    void findRoomByClientId_shouldReturnEmptyWhenNoActiveBooking() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.empty());

        Optional<Integer> result = bookingService.findRoomByClientId(clientId);

        assertFalse(result.isPresent());
        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
    }

    @Test
    void findRoomByClientId_shouldPropagateException() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class,
                () -> bookingService.findRoomByClientId(clientId));
    }

    // ==================== findActiveBookingByRoom() TESTS ====================

    @Test
    void findActiveBookingByRoom_shouldReturnBookingResponseWhenFound() {
        Integer roomNumber = 101;
        String clientId = "CL-456";
        Date checkIn = new Date();
        Date checkOut = new Date(System.currentTimeMillis() + 86400000);

        Client client = new Client("John", "Doe");
        client.setId(clientId);
        Room room = new Room();
        room.setNumber(roomNumber);
        RoomBooking booking = new RoomBooking(client, room, 150.0, checkIn, checkOut);
        booking.setId("BK-123");

        RoomBookingResponse response = new RoomBookingResponse("BK-123", clientId, roomNumber, checkIn, checkOut, 150.0);

        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.of(booking));
        when(roomBookingMapper.toResponse(booking)).thenReturn(response);

        Optional<RoomBookingResponse> result = bookingService.findActiveBookingByRoom(roomNumber);

        assertTrue(result.isPresent());
        assertEquals("BK-123", result.get().id());
        assertEquals(clientId, result.get().clientId());
        assertEquals(roomNumber, result.get().roomNumber());
        assertEquals(150.0, result.get().total());

        verify(roomBookingDAO, times(1)).findActiveByRoom(roomNumber);
        verify(roomBookingMapper, times(1)).toResponse(booking);
    }

    @Test
    void findActiveBookingByRoom_shouldReturnEmptyWhenNotFound() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.empty());

        Optional<RoomBookingResponse> result = bookingService.findActiveBookingByRoom(roomNumber);

        assertFalse(result.isPresent());
        verify(roomBookingMapper, never()).toResponse(any());
    }

    @Test
    void findActiveBookingByRoom_shouldPropagateException() {
        Integer roomNumber = 101;
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class,
                () -> bookingService.findActiveBookingByRoom(roomNumber));
    }

    // ==================== findActiveBookingByClientId() TESTS ====================

    @Test
    void findActiveBookingByClientId_shouldReturnBookingResponseWhenFound() {
        String clientId = "CL-456";
        Integer roomNumber = 101;
        Date checkIn = new Date();
        Date checkOut = new Date(System.currentTimeMillis() + 86400000);

        Client client = new Client("John", "Doe");
        client.setId(clientId);
        Room room = new Room();
        room.setNumber(roomNumber);
        RoomBooking booking = new RoomBooking(client, room, 150.0, checkIn, checkOut);
        booking.setId("BK-123");

        RoomBookingResponse response = new RoomBookingResponse("BK-123", clientId, roomNumber, checkIn, checkOut, 150.0);

        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.of(booking));
        when(roomBookingMapper.toResponse(booking)).thenReturn(response);

        Optional<RoomBookingResponse> result = bookingService.findActiveBookingByClientId(clientId);

        assertTrue(result.isPresent());
        assertEquals("BK-123", result.get().id());
        assertEquals(clientId, result.get().clientId());
        assertEquals(roomNumber, result.get().roomNumber());
        assertEquals(150.0, result.get().total());

        verify(roomBookingDAO, times(1)).findActiveByClientId(clientId);
        verify(roomBookingMapper, times(1)).toResponse(booking);
    }


    @Test
    void findActiveBookingByClientId_shouldReturnEmptyWhenNotFound() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenReturn(Optional.empty());

        Optional<RoomBookingResponse> result = bookingService.findActiveBookingByClientId(clientId);

        assertFalse(result.isPresent());
        verify(roomBookingMapper, never()).toResponse(any());
    }

    @Test
    void findActiveBookingByClientId_shouldPropagateException() {
        String clientId = "CL-123";
        when(roomBookingDAO.findActiveByClientId(clientId)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class,
                () -> bookingService.findActiveBookingByClientId(clientId));
    }
}