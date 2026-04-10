package hotel_system.controller;

import hotel_system.dto.RoomBookingResponse;
import hotel_system.service.entityService.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
    }

    @Test
    void getActiveBookingByRoom_shouldReturnBookingWhenFound() throws Exception {
        Integer roomNumber = 101;
        RoomBookingResponse response = new RoomBookingResponse("BK-1", "CL-123", roomNumber, new Date(), new Date(), 500.0);

        when(bookingService.findActiveBookingByRoom(roomNumber)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/bookings/active/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("BK-1"))
                .andExpect(jsonPath("$.clientId").value("CL-123"))
                .andExpect(jsonPath("$.roomNumber").value(101));

        verify(bookingService, times(1)).findActiveBookingByRoom(roomNumber);
    }

    @Test
    void getActiveBookingByRoom_shouldReturnNotFoundWhenMissing() throws Exception {
        Integer roomNumber = 999;

        when(bookingService.findActiveBookingByRoom(roomNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/active/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findActiveBookingByRoom(roomNumber);
    }

    @Test
    void getActiveBookingByClientId_shouldReturnBookingWhenFound() throws Exception {
        String clientId = "CL-123";
        RoomBookingResponse response = new RoomBookingResponse("BK-1", clientId, 101, new Date(), new Date(), 500.0);

        when(bookingService.findActiveBookingByClientId(clientId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/bookings/active/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("BK-1"))
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.roomNumber").value(101));

        verify(bookingService, times(1)).findActiveBookingByClientId(clientId);
    }

    @Test
    void getActiveBookingByClientId_shouldReturnNotFoundWhenMissing() throws Exception {
        String clientId = "CL-999";

        when(bookingService.findActiveBookingByClientId(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/active/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findActiveBookingByClientId(clientId);
    }

    @Test
    void getClientByRoom_shouldReturnClientWhenFound() throws Exception {
        Integer roomNumber = 101;
        hotel_system.model.entity.Client client = new hotel_system.model.entity.Client("John", "Doe");
        client.setId("CL-123");

        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/bookings/client/by-room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CL-123"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(bookingService, times(1)).findClientByRoom(roomNumber);
    }

    @Test
    void getClientByRoom_shouldReturnNotFoundWhenMissing() throws Exception {
        Integer roomNumber = 999;

        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/client/by-room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findClientByRoom(roomNumber);
    }

    @Test
    void getRoomByClientId_shouldReturnRoomNumberWhenFound() throws Exception {
        String clientId = "CL-123";
        Integer roomNumber = 101;

        when(bookingService.findRoomByClientId(clientId)).thenReturn(Optional.of(roomNumber));

        mockMvc.perform(get("/api/bookings/room/by-client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("101"));

        verify(bookingService, times(1)).findRoomByClientId(clientId);
    }

    @Test
    void getRoomByClientId_shouldReturnNotFoundWhenMissing() throws Exception {
        String clientId = "CL-999";

        when(bookingService.findRoomByClientId(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/room/by-client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findRoomByClientId(clientId);
    }
}