package hotel_system.controller;

import hotel_system.utils.HotelConfig;
import hotel_system.dto.RoomRequest;
import hotel_system.dto.RoomResponse;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.RoomService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @Mock
    private HotelConfig hotelConfig;

    @InjectMocks
    private RoomController roomController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(roomController)
                .build();
    }

    @Test
    void findRoom_shouldReturnRoomWhenFound() throws Exception {
        int roomNumber = 101;
        RoomResponse response = new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null);

        when(roomService.findRoom(roomNumber)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/rooms/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(101))
                .andExpect(jsonPath("$.type").value("STANDARD"))
                .andExpect(jsonPath("$.price").value(100.0));

        verify(roomService, times(1)).findRoom(roomNumber);
    }

    @Test
    void findRoom_shouldReturnOkWhenNotFound() throws Exception {
        int roomNumber = 999;

        when(roomService.findRoom(roomNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(roomService, times(1)).findRoom(roomNumber);
    }

    @Test
    void addRoom_shouldReturnCreatedWhenSuccess() throws Exception {
        RoomRequest request = new RoomRequest(101, RoomType.STANDARD, 100.0, 2);

        doNothing().when(roomService).addRoom(any(RoomRequest.class));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(roomService, times(1)).addRoom(any(RoomRequest.class));
    }

    @Test
    void updateRoomStatus_shouldUpdateWhenConfigEnabled() throws Exception {
        int roomNumber = 101;
        RoomCondition status = RoomCondition.CLEANING_REQUIRED;

        when(hotelConfig.isRoomStatusChangeEnabled()).thenReturn(true);
        doNothing().when(roomService).updateRoomStatus(roomNumber, status);

        mockMvc.perform(patch("/api/rooms/{roomNumber}/status", roomNumber)
                        .param("status", status.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(roomService, times(1)).updateRoomStatus(roomNumber, status);
    }

    @Test
    void updateRoomStatus_shouldThrowWhenConfigDisabled() {
        int roomNumber = 101;
        RoomCondition status = RoomCondition.CLEANING_REQUIRED;

        when(hotelConfig.isRoomStatusChangeEnabled()).thenReturn(false);

        try {
            mockMvc.perform(patch("/api/rooms/{roomNumber}/status", roomNumber)
                    .param("status", status.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            assertInstanceOf(IllegalStateException.class, e.getCause());
            assertEquals("Изменение статуса комнаты запрещено конфигурацией", e.getCause().getMessage());
        }

        verify(roomService, never()).updateRoomStatus(anyInt(), any());
    }

    @Test
    void updateRoomPrice_shouldUpdateSuccessfully() throws Exception {
        int roomNumber = 101;
        double newPrice = 150.0;

        doNothing().when(roomService).updateRoomPrice(roomNumber, newPrice);

        mockMvc.perform(patch("/api/rooms/{roomNumber}/price", roomNumber)
                        .param("newPrice", String.valueOf(newPrice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(roomService, times(1)).updateRoomPrice(roomNumber, newPrice);
    }

    @Test
    void getRooms_shouldReturnAllRooms() throws Exception {
        List<RoomResponse> rooms = Arrays.asList(
                new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null),
                new RoomResponse(102, RoomType.DELUXE, 200.0, 4, RoomCondition.READY, 5, true, null)
        );

        when(roomService.getSortedRooms(SortType.NONE)).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(101))
                .andExpect(jsonPath("$[1].number").value(102));

        verify(roomService, times(1)).getSortedRooms(SortType.NONE);
    }

    @Test
    void getRooms_shouldReturnOnlyAvailableWhenFlagTrue() throws Exception {
        List<RoomResponse> availableRooms = List.of(
                new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null)
        );

        when(roomService.getSortedAvailableRooms(SortType.PRICE)).thenReturn(availableRooms);

        mockMvc.perform(get("/api/rooms")
                        .param("sortType", "PRICE")
                        .param("onlyAvailable", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(roomService, times(1)).getSortedAvailableRooms(SortType.PRICE);
    }

    @Test
    void getAvailableRoomsByDate_shouldReturnAvailableRooms() throws Exception {
        List<RoomResponse> rooms = List.of(
                new RoomResponse(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3, true, null)
        );

        when(roomService.getAvailableRoomsByDate(any(Date.class))).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms/available")
                        .param("date", "25-03-29")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(roomService, times(1)).getAvailableRoomsByDate(any(Date.class));
    }

    @Test
    void getAvailableRoomsCount_shouldReturnCount() throws Exception {
        when(roomService.countAvailableRooms()).thenReturn(5);

        mockMvc.perform(get("/api/rooms/available/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(roomService, times(1)).countAvailableRooms();
    }

    @Test
    void isRoomAvailable_shouldReturnTrueWhenAvailable() throws Exception {
        int roomNumber = 101;

        when(roomService.isRoomAvailable(roomNumber)).thenReturn(true);

        mockMvc.perform(get("/api/rooms/{roomNumber}/available", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(roomService, times(1)).isRoomAvailable(roomNumber);
    }

    @Test
    void isRoomAvailable_shouldReturnFalseWhenNotAvailable() throws Exception {
        int roomNumber = 101;

        when(roomService.isRoomAvailable(roomNumber)).thenReturn(false);

        mockMvc.perform(get("/api/rooms/{roomNumber}/available", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(roomService, times(1)).isRoomAvailable(roomNumber);
    }
}