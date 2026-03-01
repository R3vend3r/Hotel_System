package hotel_system.controller;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.entity.Client;
import hotel_system.service.entityService.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public void registerClient(Client client) {
        try {
            clientService.registerClient(client);
        } catch (Exception e) {
            logger.error("Ошибка при регистрации клиента", e);
            throw new ManagerHotelException("Ошибка при регистрации клиента: " + e.getMessage(), e);
        }
    }

    public int getClientCount() {
        return clientService.getClientCount();
    }
    public Optional<Client> findClientByRoom(int roomNumber) {
            return clientService.findClientByRoomNumber(roomNumber);
    }
    public Optional<Client> findClientById(String clientId) {
        return clientService.findClientById(clientId);
    }

    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

}
