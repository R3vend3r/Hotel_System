package hotel_system.service.entity;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.ClientRequest;
import hotel_system.dto.ClientResponse;
import hotel_system.model.entity.Client;
import hotel_system.dao.ClientDAO;
import hotel_system.model.mapper.ClientMapper;
import hotel_system.service.entityService.BookingService;
import hotel_system.service.entityService.ClientService;
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
public class ClientServiceTest {

    @Mock
    private ClientDAO clientDAO;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private ClientService clientService;

    // ==================== registerClient() TESTS ====================

    @Test
    void registerClient_shouldCreateClientSuccessfully() {
        ClientRequest request = new ClientRequest("John", "Doe");
        Client client = new Client("John", "Doe");

        when(clientMapper.toEntity(request)).thenReturn(client);
        doNothing().when(clientDAO).create(any(Client.class));

        clientService.registerClient(request);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientDAO, times(1)).create(captor.capture());

        Client captured = captor.getValue();
        assertEquals("John", captured.getName());
        assertEquals("Doe", captured.getSurname());

        verify(clientMapper, times(1)).toEntity(request);
    }

    @Test
    void registerClient_shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> clientService.registerClient(null));
        verify(clientDAO, never()).create(any());
    }

    @Test
    void registerClient_shouldThrowServiceExceptionWhenDaoFails() {
        ClientRequest request = new ClientRequest("John", "Doe");
        Client client = new Client("John", "Doe");

        when(clientMapper.toEntity(request)).thenReturn(client);
        doThrow(new DaoException("DB error")).when(clientDAO).create(any(Client.class));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.registerClient(request));

        assertTrue(exception.getMessage().contains("Failed to register client"));
    }

    // ==================== findClientByRoomNumber() TESTS ====================

    @Test
    void findClientByRoomNumber_shouldReturnClientWhenFound() {
        Integer roomNumber = 101;
        Client client = new Client("John", "Doe");
        ClientResponse response = new ClientResponse("CL-123", "John", "Doe");

        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        Optional<ClientResponse> result = clientService.findClientByRoomNumber(roomNumber);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().name());
        assertEquals("Doe", result.get().surname());

        verify(bookingService, times(1)).findClientByRoom(roomNumber);
        verify(clientMapper, times(1)).toResponse(client);
    }

    @Test
    void findClientByRoomNumber_shouldReturnEmptyWhenNotFound() {
        Integer roomNumber = 101;
        when(bookingService.findClientByRoom(roomNumber)).thenReturn(Optional.empty());

        Optional<ClientResponse> result = clientService.findClientByRoomNumber(roomNumber);

        assertFalse(result.isPresent());
        verify(bookingService, times(1)).findClientByRoom(roomNumber);
        verify(clientMapper, never()).toResponse(any());
    }

    @Test
    void findClientByRoomNumber_shouldPropagateServiceException() {
        Integer roomNumber = 101;
        when(bookingService.findClientByRoom(roomNumber)).thenThrow(new ServiceException("Booking service error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.findClientByRoomNumber(roomNumber));

        assertEquals("Booking service error", exception.getMessage());
    }

    @Test
    void findClientByRoomNumber_shouldWrapGenericException() {
        Integer roomNumber = 101;
        when(bookingService.findClientByRoom(roomNumber)).thenThrow(new RuntimeException("Unexpected error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.findClientByRoomNumber(roomNumber));

        assertTrue(exception.getMessage().contains("Failed to find client by room: " + roomNumber));
    }

    // ==================== findByNameAndSurname() TESTS ====================

    @Test
    void findByNameAndSurname_shouldReturnClientWhenFound() {
        String name = "John";
        String surname = "Doe";
        Client client = new Client(name, surname);
        ClientResponse response = new ClientResponse("CL-123", name, surname);

        when(clientDAO.findByNameAndSurname(name, surname)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        Optional<ClientResponse> result = clientService.findByNameAndSurname(name, surname);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().name());
        assertEquals(surname, result.get().surname());

        verify(clientDAO, times(1)).findByNameAndSurname(name, surname);
    }

    @Test
    void findByNameAndSurname_shouldReturnEmptyWhenNotFound() {
        String name = "John";
        String surname = "Doe";
        when(clientDAO.findByNameAndSurname(name, surname)).thenReturn(Optional.empty());

        Optional<ClientResponse> result = clientService.findByNameAndSurname(name, surname);

        assertFalse(result.isPresent());
        verify(clientMapper, never()).toResponse(any());
    }

    @Test
    void findByNameAndSurname_shouldThrowServiceExceptionWhenDaoFails() {
        String name = "John";
        String surname = "Doe";
        when(clientDAO.findByNameAndSurname(name, surname)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.findByNameAndSurname(name, surname));

        assertTrue(exception.getMessage().contains("Failed to find client by name"));
    }

    // ==================== getClientCount() TESTS ====================

    @Test
    void getClientCount_shouldReturnNumberOfClients() {
        long expectedCount = 5L;
        when(clientDAO.count()).thenReturn(expectedCount);

        int result = clientService.getClientCount();

        assertEquals(expectedCount, result);
        verify(clientDAO, times(1)).count();
    }

    @Test
    void getClientCount_shouldReturnZeroWhenNoClients() {
        when(clientDAO.count()).thenReturn(0L);

        int result = clientService.getClientCount();

        assertEquals(0, result);
    }

    @Test
    void getClientCount_shouldThrowServiceExceptionWhenDaoFails() {
        when(clientDAO.count()).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.getClientCount());

        assertTrue(exception.getMessage().contains("Failed to get client count"));
    }

    // ==================== findClientById() TESTS ====================

    @Test
    void findClientById_shouldReturnClientWhenFound() {
        String clientId = "CL-123";
        Client client = new Client("John", "Doe");
        client.setId(clientId);
        ClientResponse response = new ClientResponse(clientId, "John", "Doe");

        when(clientDAO.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(response);

        Optional<ClientResponse> result = clientService.findClientById(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().id());
        assertEquals("John", result.get().name());

        verify(clientDAO, times(1)).findById(clientId);
    }

    @Test
    void findClientById_shouldReturnEmptyWhenNotFound() {
        String clientId = "CL-999";
        when(clientDAO.findById(clientId)).thenReturn(Optional.empty());

        Optional<ClientResponse> result = clientService.findClientById(clientId);

        assertFalse(result.isPresent());
        verify(clientMapper, never()).toResponse(any());
    }

    @Test
    void findClientById_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> clientService.findClientById(null));
        verify(clientDAO, never()).findById(any());
    }

    @Test
    void findClientById_shouldThrowServiceExceptionWhenDaoFails() {
        String clientId = "CL-123";
        when(clientDAO.findById(clientId)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.findClientById(clientId));

        assertTrue(exception.getMessage().contains("Failed to find client by ID"));
    }

    // ==================== getAllClients() TESTS ====================

    @Test
    void getAllClients_shouldReturnListOfClientResponses() {
        Client client1 = new Client("John", "Doe");
        Client client2 = new Client("Jane", "Smith");
        List<Client> clients = Arrays.asList(client1, client2);

        ClientResponse response1 = new ClientResponse("CL-1", "John", "Doe");
        ClientResponse response2 = new ClientResponse("CL-2", "Jane", "Smith");

        when(clientDAO.findAll()).thenReturn(clients);
        when(clientMapper.toResponse(client1)).thenReturn(response1);
        when(clientMapper.toResponse(client2)).thenReturn(response2);

        List<ClientResponse> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> "John".equals(r.name())));
        assertTrue(result.stream().anyMatch(r -> "Jane".equals(r.name())));

        verify(clientDAO, times(1)).findAll();
        verify(clientMapper, times(2)).toResponse(any(Client.class));
    }

    @Test
    void getAllClients_shouldReturnEmptyListWhenNoClients() {
        when(clientDAO.findAll()).thenReturn(List.of());

        List<ClientResponse> result = clientService.getAllClients();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllClients_shouldThrowServiceExceptionWhenDaoFails() {
        when(clientDAO.findAll()).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.getAllClients());

        assertTrue(exception.getMessage().contains("Failed to get all clients"));
    }

    // ==================== findClientEntityById() TESTS ====================

    @Test
    void findClientEntityById_shouldReturnClientEntityWhenFound() {
        String clientId = "CL-123";
        Client client = new Client("John", "Doe");
        client.setId(clientId);

        when(clientDAO.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.findClientEntityById(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().getId());
        assertEquals("John", result.get().getName());

        verify(clientDAO, times(1)).findById(clientId);
        verify(clientMapper, never()).toResponse(any());
    }

    @Test
    void findClientEntityById_shouldReturnEmptyWhenNotFound() {
        String clientId = "CL-999";
        when(clientDAO.findById(clientId)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.findClientEntityById(clientId);

        assertFalse(result.isPresent());
    }

    @Test
    void findClientEntityById_shouldThrowServiceExceptionWhenDaoFails() {
        String clientId = "CL-123";
        when(clientDAO.findById(clientId)).thenThrow(new DaoException("DB error"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> clientService.findClientEntityById(clientId));

        assertTrue(exception.getMessage().contains("Failed to find client"));
    }
}