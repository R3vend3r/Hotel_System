package hotel_system.service.entity;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Amenity;
import hotel_system.dao.AmenityDAO;
import hotel_system.model.mapper.AmenityMapper;
import hotel_system.service.entityService.AmenityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AmenityServiceTest {

    @Mock
    private AmenityDAO amenityDAO;

    @Mock
    private AmenityMapper amenityMapper;

    @InjectMocks
    private AmenityService amenityService;

    // ==================== addAmenity() TESTS ====================

    @Test
    void addAmenity_shouldCreateAmenitySuccessfully() {
        AmenityRequest request = new AmenityRequest("WiFi", 15.99);
        Amenity amenity = new Amenity("WiFi", 15.99);

        when(amenityMapper.toEntity(request)).thenReturn(amenity);
        doNothing().when(amenityDAO).create(any(Amenity.class));

        amenityService.addAmenity(request);

        ArgumentCaptor<Amenity> captor = ArgumentCaptor.forClass(Amenity.class);
        verify(amenityDAO, times(1)).create(captor.capture());

        Amenity captured = captor.getValue();
        assertEquals("WiFi", captured.getName());
        assertEquals(15.99, captured.getPrice());

        verify(amenityMapper, times(1)).toEntity(request);
    }

    @Test
    void addAmenity_shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> amenityService.addAmenity(null));
        verify(amenityDAO, never()).create(any());
    }

    @Test
    void addAmenity_shouldThrowServiceExceptionWhenDaoFails() {
        AmenityRequest request = new AmenityRequest("WiFi", 15.99);
        Amenity amenity = new Amenity("WiFi", 15.99);

        when(amenityMapper.toEntity(request)).thenReturn(amenity);
        doThrow(new DaoException("DB error")).when(amenityDAO).create(any(Amenity.class));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> amenityService.addAmenity(request));

        assertTrue(exception.getMessage().contains("Failed to add amenity"));
    }

    // ==================== updateAmenityPrice() TESTS ====================

    @Test
    void updateAmenityPrice_shouldUpdatePriceSuccessfully() {
        String amenityName = "WiFi";
        double newPrice = 25.99;
        Amenity existingAmenity = new Amenity("WiFi", 15.99);

        when(amenityDAO.findByName(amenityName)).thenReturn(Optional.of(existingAmenity));
        doNothing().when(amenityDAO).update(existingAmenity);

        amenityService.updateAmenityPrice(amenityName, newPrice);

        assertEquals(newPrice, existingAmenity.getPrice());
        verify(amenityDAO, times(1)).findByName(amenityName);
        verify(amenityDAO, times(1)).update(existingAmenity);
    }

    @Test
    void updateAmenityPrice_shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> amenityService.updateAmenityPrice(null, 25.99));
    }

    @Test
    void updateAmenityPrice_shouldThrowExceptionWhenAmenityNotFound() {
        String amenityName = "NonExistent";
        when(amenityDAO.findByName(amenityName)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> amenityService.updateAmenityPrice(amenityName, 25.99));

        assertEquals("Amenity not found", exception.getMessage());
    }

    @Test
    void updateAmenityPrice_shouldThrowServiceExceptionWhenDaoFails() {
        String amenityName = "WiFi";
        when(amenityDAO.findByName(amenityName)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> amenityService.updateAmenityPrice(amenityName, 25.99));

        assertTrue(exception.getMessage().contains("Failed to update amenity price"));
    }

    // ==================== getAllAmenities() TESTS ====================

    @Test
    void getAllAmenities_shouldReturnListOfAmenityResponses() {
        Amenity amenity1 = new Amenity("WiFi", 15.99);
        Amenity amenity2 = new Amenity("Pool", 50.00);
        List<Amenity> amenities = Arrays.asList(amenity1, amenity2);

        AmenityResponse response1 = new AmenityResponse("1", "WiFi", 15.99);
        AmenityResponse response2 = new AmenityResponse("2", "Pool", 50.00);

        when(amenityDAO.findAll()).thenReturn(amenities);

        when(amenityMapper.toResponse(any(Amenity.class))).thenAnswer(invocation -> {
            Amenity amenity = invocation.getArgument(0);
            if ("WiFi".equals(amenity.getName())) {
                return response1;
            } else {
                return response2;
            }
        });

        List<AmenityResponse> result = amenityService.getAllAmenities();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> "WiFi".equals(r.name())));
        assertTrue(result.stream().anyMatch(r -> "Pool".equals(r.name())));

        verify(amenityMapper, times(2)).toResponse(any(Amenity.class));

    }

    @Test
    void getAllAmenities_shouldReturnEmptyListWhenNoAmenities() {
        when(amenityDAO.findAll()).thenReturn(List.of());

        List<AmenityResponse> result = amenityService.getAllAmenities();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllAmenities_shouldThrowServiceExceptionWhenDaoFails() {
        when(amenityDAO.findAll()).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> amenityService.getAllAmenities());

        assertTrue(exception.getMessage().contains("Failed to get all amenities"));
    }

    // ==================== getSortedAmenities() TESTS ====================

    @Test
    void getSortedAmenities_shouldReturnSortedByPrice() {
        SortType sortType = SortType.PRICE;
        Amenity amenity1 = new Amenity("WiFi", 15.99);
        Amenity amenity2 = new Amenity("Pool", 50.00);
        List<Amenity> amenities = Arrays.asList(amenity1, amenity2);

        when(amenityDAO.findAllSortedByPrice()).thenReturn(amenities);
        when(amenityMapper.toResponse(amenity1)).thenReturn(new AmenityResponse("1", "WiFi", 15.99));
        when(amenityMapper.toResponse(amenity2)).thenReturn(new AmenityResponse("2", "Pool", 50.00));

        List<AmenityResponse> result = amenityService.getSortedAmenities(sortType);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(amenityDAO, times(1)).findAllSortedByPrice();
    }

    @Test
    void getSortedAmenities_shouldReturnSortedByName() {
        SortType sortType = SortType.ALPHABET;
        Amenity amenity1 = new Amenity("Pool", 50.00);
        Amenity amenity2 = new Amenity("WiFi", 15.99);
        List<Amenity> amenities = Arrays.asList(amenity1, amenity2);

        when(amenityDAO.findAllSortedByName()).thenReturn(amenities);
        when(amenityMapper.toResponse(amenity1)).thenReturn(new AmenityResponse("1", "Pool", 50.00));
        when(amenityMapper.toResponse(amenity2)).thenReturn(new AmenityResponse("2", "WiFi", 15.99));

        List<AmenityResponse> result = amenityService.getSortedAmenities(sortType);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(amenityDAO, times(1)).findAllSortedByName();
    }

    @Test
    void getSortedAmenities_shouldReturnUnsortedWhenSortTypeNone() {
        SortType sortType = SortType.NONE;
        Amenity amenity1 = new Amenity("WiFi", 15.99);
        Amenity amenity2 = new Amenity("Pool", 50.00);
        List<Amenity> amenities = Arrays.asList(amenity1, amenity2);

        when(amenityDAO.findAll()).thenReturn(amenities);
        when(amenityMapper.toResponse(amenity1)).thenReturn(new AmenityResponse("1", "WiFi", 15.99));
        when(amenityMapper.toResponse(amenity2)).thenReturn(new AmenityResponse("2", "Pool", 50.00));

        List<AmenityResponse> result = amenityService.getSortedAmenities(sortType);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(amenityDAO, times(1)).findAll();
    }

    @Test
    void getSortedAmenities_shouldThrowNullPointerExceptionWhenSortTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> amenityService.getSortedAmenities(null));
    }

    // ==================== updateAmenity() TESTS ====================

    @Test
    void updateAmenity_shouldUpdateSuccessfully() {
        String amenityId = "AM-123";
        AmenityRequest request = new AmenityRequest("Updated WiFi", 29.99);
        Amenity existingAmenity = new Amenity("Old WiFi", 15.99);
        existingAmenity.setId(amenityId);

        when(amenityDAO.findById(amenityId)).thenReturn(Optional.of(existingAmenity));
        doNothing().when(amenityDAO).update(existingAmenity);

        amenityService.updateAmenity(request, amenityId);

        assertEquals("Updated WiFi", existingAmenity.getName());
        assertEquals(29.99, existingAmenity.getPrice());
        verify(amenityDAO, times(1)).findById(amenityId);
        verify(amenityDAO, times(1)).update(existingAmenity);
    }

    @Test
    void updateAmenity_shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(NullPointerException.class,
                () -> amenityService.updateAmenity(null, "AM-123"));
    }

    @Test
    void updateAmenity_shouldThrowExceptionWhenIdIsNull() {
        AmenityRequest request = new AmenityRequest("WiFi", 15.99);

        assertThrows(NullPointerException.class,
                () -> amenityService.updateAmenity(request, null));
    }

    @Test
    void updateAmenity_shouldThrowExceptionWhenAmenityNotFound() {
        String amenityId = "NonExistent";
        AmenityRequest request = new AmenityRequest("WiFi", 15.99);

        when(amenityDAO.findById(amenityId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> amenityService.updateAmenity(request, amenityId));

        assertTrue(exception.getMessage().contains("Amenity not found with id"));
    }

    // ==================== findAmenityByName() TESTS ====================

    @Test
    void findAmenityByName_shouldReturnAmenityWhenFound() {
        String name = "WiFi";
        Amenity amenity = new Amenity("WiFi", 15.99);
        AmenityResponse response = new AmenityResponse("AM-123", "WiFi", 15.99);

        when(amenityDAO.findByName(name)).thenReturn(Optional.of(amenity));
        when(amenityMapper.toResponse(amenity)).thenReturn(response);

        Optional<AmenityResponse> result = amenityService.findAmenityByName(name);

        assertTrue(result.isPresent());
        assertEquals("WiFi", result.get().name());
    }

    @Test
    void findAmenityByName_shouldReturnEmptyWhenNotFound() {
        String name = "NonExistent";
        when(amenityDAO.findByName(name)).thenReturn(Optional.empty());

        Optional<AmenityResponse> result = amenityService.findAmenityByName(name);

        assertFalse(result.isPresent());
        verify(amenityMapper, never()).toResponse(any());
    }

    @Test
    void findAmenityByName_shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> amenityService.findAmenityByName(null));
    }

    // ==================== findAmenityById() TESTS ====================

    @Test
    void findAmenityById_shouldReturnAmenityWhenFound() {
        String id = "AM-123";
        Amenity amenity = new Amenity("WiFi", 15.99);
        amenity.setId(id);
        AmenityResponse response = new AmenityResponse(id, "WiFi", 15.99);

        when(amenityDAO.findById(id)).thenReturn(Optional.of(amenity));
        when(amenityMapper.toResponse(amenity)).thenReturn(response);

        Optional<AmenityResponse> result = amenityService.findAmenityById(id);

        assertTrue(result.isPresent());
        assertEquals("WiFi", result.get().name());
    }

    @Test
    void findAmenityById_shouldReturnEmptyWhenNotFound() {
        String id = "NonExistent";
        when(amenityDAO.findById(id)).thenReturn(Optional.empty());

        Optional<AmenityResponse> result = amenityService.findAmenityById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void findAmenityById_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> amenityService.findAmenityById(null));
    }
}