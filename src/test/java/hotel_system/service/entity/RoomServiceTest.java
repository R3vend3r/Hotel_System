package hotel_system.service.entity;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.RoomRequest;
import hotel_system.dto.RoomResponse;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Room;
import hotel_system.dao.RoomDAO;
import hotel_system.model.mapper.RoomMapper;
import hotel_system.service.entityService.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    // ==================== addRoom() TESTS ====================

    @Test
    void addRoom_shouldCreateRoomSuccessfully() {
        RoomRequest request = new RoomRequest(101, RoomType.STANDARD, 100.0, 2);
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);

        when(roomMapper.toEntity(request)).thenReturn(room);
        doNothing().when(roomDAO).create(any(Room.class));

        assertDoesNotThrow(() -> roomService.addRoom(request));

        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomDAO, times(1)).create(captor.capture());
        assertEquals(101, captor.getValue().getNumber());
        verify(roomMapper, times(1)).toEntity(request);
    }

    @Test
    void addRoom_shouldThrowServiceExceptionWhenDaoFails() {
        RoomRequest request = new RoomRequest(101, RoomType.STANDARD, 100.0, 2);
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);

        when(roomMapper.toEntity(request)).thenReturn(room);
        doThrow(new DaoException("DB error")).when(roomDAO).create(any(Room.class));

        assertThrows(ServiceException.class, () -> roomService.addRoom(request));
    }

    // ==================== findRoom() TESTS ====================

    @Test
    void findRoom_shouldReturnRoomWhenFound() {
        Integer roomNumber = 101;
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        RoomResponse response = new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null);

        when(roomDAO.findById(roomNumber)).thenReturn(Optional.of(room));
        when(roomMapper.toResponse(room)).thenReturn(response);

        Optional<RoomResponse> result = roomService.findRoom(roomNumber);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().number());
    }

    @Test
    void findRoom_shouldReturnEmptyWhenNotFound() {
        Integer roomNumber = 999;
        when(roomDAO.findById(roomNumber)).thenReturn(Optional.empty());

        Optional<RoomResponse> result = roomService.findRoom(roomNumber);

        assertFalse(result.isPresent());
    }

    @Test
    void findRoom_shouldThrowServiceExceptionWhenDaoFails() {
        Integer roomNumber = 101;
        when(roomDAO.findById(roomNumber)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> roomService.findRoom(roomNumber));
    }

    // ==================== getAllRooms() TESTS ====================

    @Test
    void getAllRooms_shouldReturnListOfRooms() {
        List<Room> rooms = Arrays.asList(
                new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3),
                new Room(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5)
        );
        RoomResponse response1 = new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null);
        RoomResponse response2 = new RoomResponse(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5, true, null);

        when(roomDAO.findAll()).thenReturn(rooms);
        when(roomMapper.toResponse(rooms.get(0))).thenReturn(response1);
        when(roomMapper.toResponse(rooms.get(1))).thenReturn(response2);

        List<RoomResponse> result = roomService.getAllRooms();

        assertEquals(2, result.size());
    }

    @Test
    void getAllRooms_shouldReturnEmptyListWhenNoRooms() {
        when(roomDAO.findAll()).thenReturn(Collections.emptyList());

        List<RoomResponse> result = roomService.getAllRooms();

        assertTrue(result.isEmpty());
    }

    // ==================== findRoomEntity() TESTS ====================

    @Test
    void findRoomEntity_shouldReturnRoomWhenFound() {
        int roomNumber = 101;
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);

        when(roomDAO.findById(roomNumber)).thenReturn(Optional.of(room));

        Optional<Room> result = roomService.findRoomEntity(roomNumber);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().getNumber());
    }

    @Test
    void findRoomEntity_shouldThrowServiceExceptionWhenDaoFails() {
        int roomNumber = 101;
        when(roomDAO.findById(roomNumber)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> roomService.findRoomEntity(roomNumber));
    }

    // ==================== countAvailableRooms() TESTS ====================

    @Test
    void countAvailableRooms_shouldReturnCount() {
        when(roomDAO.countAvailableRooms()).thenReturn(5);

        int result = roomService.countAvailableRooms();

        assertEquals(5, result);
    }

    @Test
    void countAvailableRooms_shouldThrowServiceExceptionWhenDaoFails() {
        when(roomDAO.countAvailableRooms()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> roomService.countAvailableRooms());
    }

    // ==================== isRoomAvailable() TESTS ====================

    @Test
    void isRoomAvailable_shouldReturnTrueWhenAvailable() {
        int roomNumber = 101;
        when(roomDAO.isRoomAvailable(roomNumber)).thenReturn(true);

        boolean result = roomService.isRoomAvailable(roomNumber);

        assertTrue(result);
    }

    @Test
    void isRoomAvailable_shouldReturnFalseWhenNotAvailable() {
        int roomNumber = 101;
        when(roomDAO.isRoomAvailable(roomNumber)).thenReturn(false);

        boolean result = roomService.isRoomAvailable(roomNumber);

        assertFalse(result);
    }

    // ==================== getAvailableRoomsByDate() TESTS ====================

    @Test
    void getAvailableRoomsByDate_shouldReturnAvailableRooms() {
        Date date = new Date();
        Room room1 = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        room1.setAvailable(true);
        Room room2 = new Room(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5);
        room2.setAvailable(false);
        room2.setAvailableDate(new Date(System.currentTimeMillis() - 86400000));

        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomDAO.findAll()).thenReturn(rooms);
        when(roomMapper.toResponse(any(Room.class))).thenReturn(mock(RoomResponse.class));

        List<RoomResponse> result = roomService.getAvailableRoomsByDate(date);

        assertEquals(2, result.size());
    }

    // ==================== getSortedRooms() TESTS ====================

    @Test
    void getSortedRooms_shouldReturnSortedByPrice() {
        List<Room> rooms = Arrays.asList(
                new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3),
                new Room(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5)
        );
        when(roomDAO.findAll()).thenReturn(rooms);
        when(roomMapper.toResponse(any(Room.class))).thenReturn(mock(RoomResponse.class));

        List<RoomResponse> result = roomService.getSortedRooms(SortType.PRICE);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getSortedRooms_shouldReturnUnsortedWhenNone() {
        List<Room> rooms = Arrays.asList(
                new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3),
                new Room(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5)
        );
        when(roomDAO.findAll()).thenReturn(rooms);
        when(roomMapper.toResponse(any(Room.class))).thenReturn(mock(RoomResponse.class));

        List<RoomResponse> result = roomService.getSortedRooms(SortType.NONE);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ==================== getSortedAvailableRooms() TESTS ====================

    @Test
    void getSortedAvailableRooms_shouldReturnOnlyAvailable() {
        Room room1 = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        room1.setAvailable(true);
        Room room2 = new Room(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5);
        room2.setAvailable(false);

        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomDAO.findAll()).thenReturn(rooms);
        when(roomMapper.toResponse(any(Room.class))).thenReturn(mock(RoomResponse.class));

        List<RoomResponse> result = roomService.getSortedAvailableRooms(SortType.PRICE);

        assertEquals(1, result.size());
    }

    // ==================== occupyRoom() TESTS ====================

    @Test
    void occupyRoom_shouldOccupySuccessfully() {
        int roomNumber = 101;
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        room.setAvailable(true);

        when(roomDAO.findById(roomNumber)).thenReturn(Optional.of(room));
        doNothing().when(roomDAO).update(room);

        assertDoesNotThrow(() -> roomService.occupyRoom(roomNumber));

        assertFalse(room.isAvailable());
        verify(roomDAO, times(1)).update(room);
    }

    @Test
    void occupyRoom_shouldThrowWhenRoomNotFound() {
        int roomNumber = 999;
        when(roomDAO.findById(roomNumber)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> roomService.occupyRoom(roomNumber));
    }

    @Test
    void occupyRoom_shouldThrowWhenRoomAlreadyOccupied() {
        int roomNumber = 101;
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        room.setAvailable(false);

        when(roomDAO.findById(roomNumber)).thenReturn(Optional.of(room));

        assertThrows(ServiceException.class, () -> roomService.occupyRoom(roomNumber));
    }

    // ==================== vacateRoom() TESTS ====================

    @Test
    void vacateRoom_shouldVacateSuccessfully() {
        int roomNumber = 101;
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        room.setAvailable(false);

        when(roomDAO.findById(roomNumber)).thenReturn(Optional.of(room));
        doNothing().when(roomDAO).update(room);

        assertDoesNotThrow(() -> roomService.vacateRoom(roomNumber));

        assertTrue(room.isAvailable());
        assertNull(room.getAvailableDate());
        verify(roomDAO, times(1)).update(room);
    }

    @Test
    void vacateRoom_shouldThrowWhenRoomNotFound() {
        int roomNumber = 999;
        when(roomDAO.findById(roomNumber)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> roomService.vacateRoom(roomNumber));
    }

    // ==================== updateRoomStatus() TESTS ====================

    @Test
    void updateRoomStatus_shouldUpdateSuccessfully() {
        int roomNumber = 101;
        RoomCondition newStatus = RoomCondition.CLEANING_REQUIRED;

        doNothing().when(roomDAO).updateRoomStatus(roomNumber, newStatus);

        assertDoesNotThrow(() -> roomService.updateRoomStatus(roomNumber, newStatus));
        verify(roomDAO, times(1)).updateRoomStatus(roomNumber, newStatus);
    }

    @Test
    void updateRoomStatus_shouldThrowServiceExceptionWhenDaoFails() {
        int roomNumber = 101;
        RoomCondition newStatus = RoomCondition.ON_REPAIR;

        doThrow(new DaoException("DB error")).when(roomDAO).updateRoomStatus(roomNumber, newStatus);

        assertThrows(ServiceException.class, () -> roomService.updateRoomStatus(roomNumber, newStatus));
    }

    // ==================== updateRoomPrice() TESTS ====================

    @Test
    void updateRoomPrice_shouldUpdateSuccessfully() {
        int roomNumber = 101;
        double newPrice = 150.0;

        doNothing().when(roomDAO).updateRoomPrice(roomNumber, newPrice);

        assertDoesNotThrow(() -> roomService.updateRoomPrice(roomNumber, newPrice));
        verify(roomDAO, times(1)).updateRoomPrice(roomNumber, newPrice);
    }

    @Test
    void updateRoomPrice_shouldThrowServiceExceptionWhenDaoFails() {
        int roomNumber = 101;
        double newPrice = 150.0;

        doThrow(new DaoException("DB error")).when(roomDAO).updateRoomPrice(roomNumber, newPrice);

        assertThrows(ServiceException.class, () -> roomService.updateRoomPrice(roomNumber, newPrice));
    }
}