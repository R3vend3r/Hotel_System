package hotel_system.controller;

import hotel_system.dto.ClientRequest;
import hotel_system.dto.ClientResponse;
import hotel_system.service.entityService.ClientService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .build();
    }

    @Test
    void registerClient_shouldReturnCreatedWhenSuccess() throws Exception {
        ClientRequest request = new ClientRequest("John", "Doe");

        doNothing().when(clientService).registerClient(any(ClientRequest.class));

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(clientService, times(1)).registerClient(any(ClientRequest.class));
    }

    @Test
    void getClientCount_shouldReturnCount() throws Exception {
        when(clientService.getClientCount()).thenReturn(5);

        mockMvc.perform(get("/api/clients/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(clientService, times(1)).getClientCount();
    }

    @Test
    void findClientByRoom_shouldReturnClientWhenFound() throws Exception {
        int roomNumber = 101;
        ClientResponse response = new ClientResponse("CL-123", "John", "Doe");

        when(clientService.findClientByRoomNumber(roomNumber)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/clients/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CL-123"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(clientService, times(1)).findClientByRoomNumber(roomNumber);
    }

    @Test
    void findClientByRoom_shouldReturnOkWithEmptyWhenNotFound() throws Exception {
        int roomNumber = 999;

        when(clientService.findClientByRoomNumber(roomNumber)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/room/{roomNumber}", roomNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(clientService, times(1)).findClientByRoomNumber(roomNumber);
    }

    @Test
    void findByNameAndSurname_shouldReturnClientWhenFound() throws Exception {
        String name = "John";
        String surname = "Doe";
        ClientResponse response = new ClientResponse("CL-123", "John", "Doe");

        when(clientService.findByNameAndSurname(name, surname)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/clients/search")
                        .param("name", name)
                        .param("surname", surname)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CL-123"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(clientService, times(1)).findByNameAndSurname(name, surname);
    }

    @Test
    void findByNameAndSurname_shouldReturnOkWithEmptyWhenNotFound() throws Exception {
        String name = "NonExistent";
        String surname = "User";

        when(clientService.findByNameAndSurname(name, surname)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/search")
                        .param("name", name)
                        .param("surname", surname)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(clientService, times(1)).findByNameAndSurname(name, surname);
    }

    @Test
    void findClientById_shouldReturnClientWhenFound() throws Exception {
        String clientId = "CL-123";
        ClientResponse response = new ClientResponse("CL-123", "John", "Doe");

        when(clientService.findClientById(clientId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/clients/id/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CL-123"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"));

        verify(clientService, times(1)).findClientById(clientId);
    }

    @Test
    void findClientById_shouldReturnOkWithEmptyWhenNotFound() throws Exception {
        String clientId = "CL-999";

        when(clientService.findClientById(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/id/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(clientService, times(1)).findClientById(clientId);
    }

    @Test
    void getAllClients_shouldReturnListOfClients() throws Exception {
        List<ClientResponse> clients = Arrays.asList(
                new ClientResponse("CL-1", "John", "Doe"),
                new ClientResponse("CL-2", "Jane", "Smith")
        );

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("CL-1"))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].id").value("CL-2"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void getAllClients_shouldReturnEmptyListWhenNoClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(List.of());

        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(clientService, times(1)).getAllClients();
    }
}