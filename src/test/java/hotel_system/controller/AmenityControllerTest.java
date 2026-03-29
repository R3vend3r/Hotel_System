package hotel_system.controller;

import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.AmenityService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AmenityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AmenityService amenityService;

    @InjectMocks
    private AmenityController amenityController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(amenityController)
                .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                .build();
    }

    @Test
    void addAmenity_shouldReturnCreatedWhenSuccess() throws Exception {
        AmenityRequest request = new AmenityRequest("WiFi", 15.99);

        doNothing().when(amenityService).addAmenity(any(AmenityRequest.class));

        mockMvc.perform(post("/api/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(amenityService, times(1)).addAmenity(any(AmenityRequest.class));
    }

    @Test
    void findAmenityByName_shouldReturnAmenityWhenFound() throws Exception {
        String name = "WiFi";
        AmenityResponse response = new AmenityResponse("AM-1", "WiFi", 15.99);

        when(amenityService.findAmenityByName(name)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/amenities/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenityId").value("AM-1"))
                .andExpect(jsonPath("$.name").value("WiFi"))
                .andExpect(jsonPath("$.price").value(15.99));

        verify(amenityService, times(1)).findAmenityByName(name);
    }

    @Test
    void findAmenityByName_shouldReturnNotFoundWhenMissing() throws Exception {
        String name = "NonExistent";

        when(amenityService.findAmenityByName(name)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/amenities/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));

        verify(amenityService, times(1)).findAmenityByName(name);
    }

    @Test
    void findAmenityById_shouldReturnAmenityWhenFound() throws Exception {
        String id = "AM-1";
        AmenityResponse response = new AmenityResponse("AM-1", "WiFi", 15.99);

        when(amenityService.findAmenityById(id)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/amenities/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenityId").value("AM-1"))
                .andExpect(jsonPath("$.name").value("WiFi"))
                .andExpect(jsonPath("$.price").value(15.99));

        verify(amenityService, times(1)).findAmenityById(id);
    }

    @Test
    void findAmenityById_shouldReturnNotFoundWhenMissing() throws Exception {
        String id = "AM-999";

        when(amenityService.findAmenityById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/amenities/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));

        verify(amenityService, times(1)).findAmenityById(id);
    }

    @Test
    void getAmenities_shouldReturnAllAmenitiesWithoutSort() throws Exception {
        List<AmenityResponse> amenities = Arrays.asList(
                new AmenityResponse("AM-1", "WiFi", 15.99),
                new AmenityResponse("AM-2", "Pool", 50.00)
        );

        when(amenityService.getSortedAmenities(SortType.NONE)).thenReturn(amenities);

        mockMvc.perform(get("/api/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amenityId").value("AM-1"))
                .andExpect(jsonPath("$[0].name").value("WiFi"))
                .andExpect(jsonPath("$[1].amenityId").value("AM-2"))
                .andExpect(jsonPath("$[1].name").value("Pool"));

        verify(amenityService, times(1)).getSortedAmenities(SortType.NONE);
    }

    @Test
    void getAmenities_shouldReturnSortedByPrice() throws Exception {
        List<AmenityResponse> amenities = Arrays.asList(
                new AmenityResponse("AM-2", "Pool", 50.00),
                new AmenityResponse("AM-1", "WiFi", 15.99)
        );

        when(amenityService.getSortedAmenities(SortType.PRICE)).thenReturn(amenities);

        mockMvc.perform(get("/api/amenities")
                        .param("sortType", "PRICE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amenityId").value("AM-2"))
                .andExpect(jsonPath("$[1].amenityId").value("AM-1"));

        verify(amenityService, times(1)).getSortedAmenities(SortType.PRICE);
    }

    @Test
    void getAmenities_shouldReturnEmptyListWhenNoAmenities() throws Exception {
        when(amenityService.getSortedAmenities(SortType.NONE)).thenReturn(List.of());

        mockMvc.perform(get("/api/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(amenityService, times(1)).getSortedAmenities(SortType.NONE);
    }

    @Test
    void updateAmenityPrice_shouldUpdateSuccessfully() throws Exception {
        String amenityName = "WiFi";
        double newPrice = 25.99;

        doNothing().when(amenityService).updateAmenityPrice(amenityName, newPrice);

        mockMvc.perform(patch("/api/amenities/{amenityName}/price", amenityName)
                        .param("newPrice", String.valueOf(newPrice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(amenityService, times(1)).updateAmenityPrice(amenityName, newPrice);
    }
}