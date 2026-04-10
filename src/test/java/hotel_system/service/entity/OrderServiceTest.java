package hotel_system.service.entity;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;
import hotel_system.model.entity.*;
import hotel_system.dao.AmenityOrderDAO;
import hotel_system.dao.RoomBookingDAO;
import hotel_system.enums.SortType;
import hotel_system.model.mapper.AmenityOrderMapper;
import hotel_system.model.mapper.RoomBookingMapper;
import hotel_system.service.entityService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private RoomService roomService;
    @Mock
    private ClientService clientService;
    @Mock
    private BookingService bookingService;
    @Mock
    private RoomBookingDAO roomBookingDAO;
    @Mock
    private AmenityService amenityService;
    @Mock
    private AmenityOrderDAO amenityOrderDAO;
    @Mock
    private RoomBookingMapper roomBookingMapper;
    @Mock
    private AmenityOrderMapper amenityOrderMapper;

    @InjectMocks
    private OrderService orderService;

    // ==================== settleClient() TESTS ====================

    @Test
    void settleClient_shouldSettleClientSuccessfully() {
        SettleClientRequest request = new SettleClientRequest("CL-123", 101, new Date());
        ClientResponse clientResponse = new ClientResponse("CL-123", "John", "Doe");
        RoomResponse roomResponse = new RoomResponse(
                101, RoomType.STANDARD,100.0,2, RoomCondition.READY,3,true,new Date());
        Client clientEntity = new Client("John", "Doe");
        clientEntity.setId("CL-123");
        Room roomEntity = new Room();
        roomEntity.setNumber(101);

        when(clientService.findClientById("CL-123")).thenReturn(Optional.of(clientResponse));
        when(roomService.findRoom(101)).thenReturn(Optional.of(roomResponse));
        when(roomService.isRoomAvailable(101)).thenReturn(true);
        when(clientService.findClientEntityById("CL-123")).thenReturn(Optional.of(clientEntity));
        when(roomService.findRoomEntity(101)).thenReturn(Optional.of(roomEntity));
        doNothing().when(roomService).occupyRoom(101);
        doNothing().when(roomBookingDAO).create(any(RoomBooking.class));

        assertDoesNotThrow(() -> orderService.settleClient(request));

        verify(roomBookingDAO, times(1)).create(any(RoomBooking.class));
        verify(roomService, times(1)).occupyRoom(101);
    }


    @Test
    void settleClient_shouldThrowWhenClientNotFound() {
        SettleClientRequest request = new SettleClientRequest("CL-999", 101, new Date());
        when(clientService.findClientById("CL-999")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.settleClient(request));
    }

    @Test
    void settleClient_shouldThrowWhenRoomNotFound() {
        SettleClientRequest request = new SettleClientRequest("CL-123", 999, new Date());
        when(clientService.findClientById("CL-123")).thenReturn(Optional.of(new ClientResponse("CL-123", "John", "Doe")));
        when(roomService.findRoom(999)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.settleClient(request));
    }

    @Test
    void settleClient_shouldThrowWhenRoomNotAvailable() {
        SettleClientRequest request = new SettleClientRequest("CL-123", 101, new Date());
        ClientResponse clientResponse = new ClientResponse("CL-123", "John", "Doe");
        RoomResponse roomResponse = new RoomResponse(
                101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, false, new Date()
        );

        when(clientService.findClientById("CL-123")).thenReturn(Optional.of(clientResponse));
        when(roomService.findRoom(101)).thenReturn(Optional.of(roomResponse));
        when(roomService.isRoomAvailable(101)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> orderService.settleClient(request));
    }
    // ==================== addAmenityToBooking() TESTS ====================

    @Test
    void addAmenityToBooking_shouldAddAmenitySuccessfully() {
        AddAmenityRequest request = new AddAmenityRequest("CL-123", "AM-1", new Date());
        AmenityResponse amenityResponse = new AmenityResponse("AM-1", "WiFi", 15.99);
        ClientResponse clientResponse = new ClientResponse("CL-123", "John", "Doe");
        RoomBooking activeBooking = mock(RoomBooking.class);

        when(amenityService.findAmenityById("AM-1")).thenReturn(Optional.of(amenityResponse));
        when(clientService.findClientById("CL-123")).thenReturn(Optional.of(clientResponse));
        when(bookingService.findActiveBookingEntityByClientId("CL-123")).thenReturn(Optional.of(activeBooking));
        when(roomBookingDAO.findActiveByClientId("CL-123")).thenReturn(Optional.of(activeBooking));

        when(activeBooking.getTotalPrice()).thenReturn(100.0);
        doNothing().when(amenityOrderDAO).create(any(AmenityOrder.class));
        doNothing().when(roomBookingDAO).update(activeBooking);

        assertDoesNotThrow(() -> orderService.addAmenityToBooking(request));

        verify(amenityOrderDAO, times(1)).create(any(AmenityOrder.class));
        verify(roomBookingDAO, times(1)).update(activeBooking);
    }

    @Test
    void addAmenityToBooking_shouldThrowWhenAmenityNotFound() {
        AddAmenityRequest request = new AddAmenityRequest("CL-123","AM-999", new Date());
        when(amenityService.findAmenityById("AM-999")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.addAmenityToBooking(request));
    }

    @Test
    void addAmenityToBooking_shouldThrowWhenClientNotFound() {
        AddAmenityRequest request = new AddAmenityRequest("CL-999","AM-1",  new Date());
        when(amenityService.findAmenityById("AM-1")).thenReturn(Optional.of(new AmenityResponse("AM-1", "WiFi", 15.99)));
        when(clientService.findClientById("CL-999")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.addAmenityToBooking(request));
    }

    @Test
    void addAmenityToBooking_shouldThrowWhenNoActiveBooking() {
        AddAmenityRequest request = new AddAmenityRequest("CL-123", "AM-1", new Date());
        when(amenityService.findAmenityById("AM-1")).thenReturn(Optional.of(new AmenityResponse("AM-1", "WiFi", 15.99)));
        when(clientService.findClientById("CL-123")).thenReturn(Optional.of(new ClientResponse("CL-123", "John", "Doe")));
        when(bookingService.findActiveBookingEntityByClientId("CL-123")).thenReturn(Optional.of(mock(RoomBooking.class)));
        when(roomBookingDAO.findActiveByClientId("CL-123")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.addAmenityToBooking(request));
    }

    // ==================== evictClient() TESTS ====================

    @Test
    void evictClient_shouldEvictClientSuccessfully() {
        int roomNumber = 101;
        Client client = new Client("John", "Doe");
        client.setId("CL-123");
        RoomBooking booking = mock(RoomBooking.class);

        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.of(client));
        when(roomBookingDAO.findActiveByRoom(roomNumber)).thenReturn(Optional.of(booking));
        doNothing().when(roomBookingDAO).update(booking);
        doNothing().when(roomService).vacateRoom(roomNumber);

        assertDoesNotThrow(() -> orderService.evictClient(roomNumber));

        verify(roomService, times(1)).vacateRoom(roomNumber);
        verify(roomBookingDAO, times(1)).update(booking);
    }

    @Test
    void evictClient_shouldThrowWhenRoomEmpty() {
        int roomNumber = 101;
        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> orderService.evictClient(roomNumber));
    }

    // ==================== calculateRoomPayment() TESTS ====================

    @Test
    void calculateRoomPayment_shouldReturnTotalPayment() {
        int roomNumber = 101;
        when(roomBookingDAO.calculateStayCost(roomNumber)).thenReturn(500.0);
        when(amenityOrderDAO.calculateTotalForRoom(roomNumber)).thenReturn(50.0);

        double result = orderService.calculateRoomPayment(roomNumber);

        assertEquals(550.0, result);
    }

    @Test
    void calculateRoomPayment_shouldThrowWhenDaoFails() {
        int roomNumber = 101;
        when(roomBookingDAO.calculateStayCost(roomNumber)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.calculateRoomPayment(roomNumber));
    }

    // ==================== calculateTotalRevenue() TESTS ====================

    @Test
    void calculateTotalRevenue_shouldReturnTotalRevenue() {
        when(roomBookingDAO.calculateTotalIncome()).thenReturn(1000.0);
        when(amenityOrderDAO.calculateTotalIncome()).thenReturn(200.0);

        double result = orderService.calculateTotalRevenue();

        assertEquals(1200.0, result);
    }

    @Test
    void calculateTotalRevenue_shouldThrowWhenDaoFails() {
        when(roomBookingDAO.calculateTotalIncome()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.calculateTotalRevenue());
    }

    // ==================== getActiveBookingsSorted() TESTS ====================

    @Test
    void getActiveBookingsSorted_shouldReturnSortedBookings() {
        List<RoomBooking> bookings = Arrays.asList(mock(RoomBooking.class), mock(RoomBooking.class));
        when(roomBookingDAO.findActiveBookings()).thenReturn(bookings);
        when(roomBookingMapper.toResponse(any())).thenReturn(mock(RoomBookingResponse.class));

        List<RoomBookingResponse> result = orderService.getActiveBookingsSorted(SortType.NONE);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getActiveBookingsSorted_shouldThrowWhenDaoFails() {
        when(roomBookingDAO.findActiveBookings()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.getActiveBookingsSorted(SortType.NONE));
    }

    // ==================== getCompletedBookings() TESTS ====================

    @Test
    void getCompletedBookings_shouldReturnCompletedBookings() {
        List<RoomBooking> bookings = Arrays.asList(mock(RoomBooking.class), mock(RoomBooking.class));
        when(roomBookingDAO.findCompletedBookings()).thenReturn(bookings);
        when(roomBookingMapper.toResponse(any())).thenReturn(mock(RoomBookingResponse.class));

        List<RoomBookingResponse> result = orderService.getCompletedBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getCompletedBookings_shouldThrowWhenDaoFails() {
        when(roomBookingDAO.findCompletedBookings()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.getCompletedBookings());
    }

    // ==================== getAmenityOrdersSorted() TESTS ====================

    @Test
    void getAmenityOrdersSorted_shouldReturnSortedOrders() {
        List<AmenityOrder> orders = Arrays.asList(mock(AmenityOrder.class), mock(AmenityOrder.class));
        when(amenityOrderDAO.findAll()).thenReturn(orders);
        when(amenityOrderMapper.toResponse(any())).thenReturn(mock(AmenityOrderResponse.class));

        List<AmenityOrderResponse> result = orderService.getAmenityOrdersSorted(SortType.NONE);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAmenityOrdersSorted_shouldThrowWhenDaoFails() {
        when(amenityOrderDAO.findAll()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.getAmenityOrdersSorted(SortType.NONE));
    }

    // ==================== getClientAmenitiesSorted() TESTS ====================

    @Test
    void getClientAmenitiesSorted_shouldReturnClientOrders() {
        String clientId = "CL-123";
        AmenityOrder order = mock(AmenityOrder.class);
        when(order.getClientId()).thenReturn(clientId);
        List<AmenityOrder> orders = Arrays.asList(order, mock(AmenityOrder.class));
        when(amenityOrderDAO.findAll()).thenReturn(orders);
        when(amenityOrderMapper.toResponse(any())).thenReturn(mock(AmenityOrderResponse.class));

        List<AmenityOrderResponse> result = orderService.getClientAmenitiesSorted(clientId, SortType.NONE);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getClientAmenitiesSorted_shouldThrowWhenDaoFails() {
        String clientId = "CL-123";
        when(amenityOrderDAO.findAll()).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.getClientAmenitiesSorted(clientId, SortType.NONE));
    }

    // ==================== getLastThreeBookingsForRoom() TESTS ====================

    @Test
    void getLastThreeBookingsForRoom_shouldReturnThreeBookings() {
        int roomNumber = 101;
        List<RoomBooking> bookings = Arrays.asList(mock(RoomBooking.class), mock(RoomBooking.class), mock(RoomBooking.class));
        when(roomBookingDAO.findByRoom(roomNumber, 3)).thenReturn(bookings);
        when(roomBookingMapper.toResponse(any())).thenReturn(mock(RoomBookingResponse.class));

        List<RoomBookingResponse> result = orderService.getLastThreeBookingsForRoom(roomNumber);

        assertEquals(3, result.size());
    }

    @Test
    void getLastThreeBookingsForRoom_shouldThrowWhenDaoFails() {
        int roomNumber = 101;
        when(roomBookingDAO.findByRoom(roomNumber, 3)).thenThrow(new DaoException("DB error"));

        assertThrows(ServiceException.class, () -> orderService.getLastThreeBookingsForRoom(roomNumber));
    }

    // ==================== getRoomHistory() TESTS ====================

    @Test
    void getRoomHistory_shouldReturnClientHistory() {
        int roomNumber = 101;
        Client client = new Client("John", "Doe");
        client.setId("CL-123");
        RoomBooking booking = mock(RoomBooking.class);
        when(booking.getClient()).thenReturn(client);
        List<RoomBooking> bookings = List.of(booking);
        when(roomBookingDAO.findAllByRoom(roomNumber)).thenReturn(bookings);

        List<ClientResponse> result = orderService.getRoomHistory(roomNumber);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).name());
    }
}