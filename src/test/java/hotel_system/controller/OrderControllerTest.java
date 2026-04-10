package hotel_system.controller;

import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
    }

    @Test
    void addAmenityToClient_shouldReturnCreatedWhenSuccess() throws Exception {
        AddAmenityRequest request = new AddAmenityRequest("CL-123", "AM-1", new Date());

        doNothing().when(orderService).addAmenityToBooking(any(AddAmenityRequest.class));

        mockMvc.perform(post("/api/orders/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).addAmenityToBooking(any(AddAmenityRequest.class));
    }

    @Test
    void settleClient_shouldReturnCreatedWhenSuccess() throws Exception {
        SettleClientRequest request = new SettleClientRequest("CL-123", 101, new Date());

        doNothing().when(orderService).settleClient(any(SettleClientRequest.class));

        mockMvc.perform(post("/api/orders/settle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).settleClient(any(SettleClientRequest.class));
    }

    @Test
    void evictClient_shouldReturnOkWhenSuccess() throws Exception {
        Integer roomNumber = 101;

        doNothing().when(orderService).evictClient(roomNumber);

        mockMvc.perform(post("/api/orders/evict/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).evictClient(roomNumber);
    }

    @Test
    void getAllActiveBookings_shouldReturnListOfBookings() throws Exception {
        List<RoomBookingResponse> bookings = Arrays.asList(
                new RoomBookingResponse("BK-1", "CL-123", 101, new Date(), new Date(), 500.0),
                new RoomBookingResponse("BK-2", "CL-456", 102, new Date(), new Date(), 300.0)
        );

        when(orderService.getActiveBookingsSorted(SortType.NONE)).thenReturn(bookings);

        mockMvc.perform(get("/api/orders/bookings/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("BK-1"))
                .andExpect(jsonPath("$[1].id").value("BK-2"));

        verify(orderService, times(1)).getActiveBookingsSorted(SortType.NONE);
    }

    @Test
    void getAllActiveBookings_shouldReturnSortedByDateEnd() throws Exception {
        List<RoomBookingResponse> bookings = Arrays.asList(
                new RoomBookingResponse("BK-1", "CL-123", 101, new Date(), new Date(), 500.0),
                new RoomBookingResponse("BK-2", "CL-456", 102, new Date(), new Date(), 300.0)
        );

        when(orderService.getActiveBookingsSorted(SortType.DATE_END)).thenReturn(bookings);

        mockMvc.perform(get("/api/orders/bookings/active")
                        .param("sortType", "DATE_END")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(orderService, times(1)).getActiveBookingsSorted(SortType.DATE_END);
    }

    @Test
    void getAllCompletedBookings_shouldReturnListOfCompletedBookings() throws Exception {
        List<RoomBookingResponse> bookings = List.of(
                new RoomBookingResponse("BK-1", "CL-123", 101, new Date(), new Date(), 500.0)
        );

        when(orderService.getCompletedBookings()).thenReturn(bookings);

        mockMvc.perform(get("/api/orders/bookings/completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("BK-1"));

        verify(orderService, times(1)).getCompletedBookings();
    }

    @Test
    void getClientAmenitiesSorted_shouldReturnListOfAmenities() throws Exception {
        String clientId = "CL-123";
        List<AmenityOrderResponse> amenities = List.of(
                new AmenityOrderResponse("ORD-1", clientId, 100.0, "AM-1", new Date(), new Date(), new Date())
        );

        when(orderService.getClientAmenitiesSorted(clientId, SortType.NONE)).thenReturn(amenities);

        mockMvc.perform(get("/api/orders/amenities/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ORD-1"));

        verify(orderService, times(1)).getClientAmenitiesSorted(clientId, SortType.NONE);
    }

    @Test
    void getLastThreeBookingsForRoom_shouldReturnListOfBookings() throws Exception {
        int roomNumber = 101;
        List<RoomBookingResponse> bookings = Arrays.asList(
                new RoomBookingResponse("BK-1", "CL-123", roomNumber, new Date(), new Date(), 500.0),
                new RoomBookingResponse("BK-2", "CL-456", roomNumber, new Date(), new Date(), 300.0),
                new RoomBookingResponse("BK-3", "CL-789", roomNumber, new Date(), new Date(), 200.0)
        );

        when(orderService.getLastThreeBookingsForRoom(roomNumber)).thenReturn(bookings);

        mockMvc.perform(get("/api/orders/bookings/room/{roomNumber}/last", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        verify(orderService, times(1)).getLastThreeBookingsForRoom(roomNumber);
    }

    @Test
    void calculateRoomPayment_shouldReturnPaymentAmount() throws Exception {
        int roomNumber = 101;
        double expectedPayment = 550.0;

        when(orderService.calculateRoomPayment(roomNumber)).thenReturn(expectedPayment);

        mockMvc.perform(get("/api/orders/payment/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("550.0"));

        verify(orderService, times(1)).calculateRoomPayment(roomNumber);
    }

    @Test
    void calculateTotalRevenue_shouldReturnTotalRevenue() throws Exception {
        double expectedRevenue = 10000.0;

        when(orderService.calculateTotalRevenue()).thenReturn(expectedRevenue);

        mockMvc.perform(get("/api/orders/revenue/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("10000.0"));

        verify(orderService, times(1)).calculateTotalRevenue();
    }

    @Test
    void getRoomHistory_shouldReturnListOfClients() throws Exception {
        int roomNumber = 101;
        List<ClientResponse> history = Arrays.asList(
                new ClientResponse("CL-1", "John", "Doe"),
                new ClientResponse("CL-2", "Jane", "Smith")
        );

        when(orderService.getRoomHistory(roomNumber)).thenReturn(history);

        mockMvc.perform(get("/api/orders/history/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("CL-1"))
                .andExpect(jsonPath("$[1].id").value("CL-2"));

        verify(orderService, times(1)).getRoomHistory(roomNumber);
    }
}