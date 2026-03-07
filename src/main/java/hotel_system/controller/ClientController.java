package hotel_system.controller;

import hotel_system.dto.ClientRequest;
import hotel_system.dto.ClientResponse;
import hotel_system.service.entityService.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public void registerClient(@RequestBody ClientRequest request) {
        clientService.registerClient(request);
    }

    @GetMapping("/count")
    public int getClientCount() {
        return clientService.getClientCount();
    }


    @GetMapping("/room/{roomNumber}")
    public Optional<ClientResponse> findClientByRoom(@PathVariable int roomNumber) {
        return clientService.findClientByRoomNumber(roomNumber);
    }

    @GetMapping("/search")
    public Optional<ClientResponse> findByNameAndSurname(
            @RequestParam String name,
            @RequestParam String surname) {
        return clientService.findByNameAndSurname(name, surname);
    }

    @GetMapping("/id/{clientId}")
    public Optional<ClientResponse> findClientById(@PathVariable String clientId) {
        return clientService.findClientById(clientId);
    }

    @GetMapping
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }
}