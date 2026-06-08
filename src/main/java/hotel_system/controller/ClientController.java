package hotel_system.controller;

import hotel_system.dto.ClientRequest;
import hotel_system.dto.ClientResponse;
import hotel_system.service.entityService.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void registerClient(@Valid @RequestBody ClientRequest request) {
        clientService.registerClient(request);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public int getClientCount() {
        return clientService.getClientCount();
    }

    @GetMapping("/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClientResponse> findClientByRoom(@PathVariable int roomNumber) {
        return clientService.findClientByRoomNumber(roomNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClientResponse> findByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname) {
        return clientService.findByNameAndSurname(name, surname)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClientResponse> findClientById(@PathVariable String clientId) {
        return clientService.findClientById(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }
}