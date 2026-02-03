package hotelsystem.service.entityService;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.model.Client;
import hotelsystem.dao.ClientDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Inject
    private ClientDAO clientDAO;

    public void registerClient(Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        try {
            clientDAO.create(client);
        } catch (Exception e) {
            logger.error("Failed to register client", e);
            throw new RuntimeException("Failed to register client", e);
        }
    }

    public Optional<Client> findClientByRoomNumber(int roomNumber) {
        try {
            return clientDAO.findByRoomNumber(roomNumber);
        } catch (Exception e) {
            logger.error("Failed to find client by room", e);
            throw new RuntimeException("Failed to find client by room", e);
        }
    }

    public int getClientCount(){
        try {
            List<Client> clients = clientDAO.findAll();
            return clients.size();
        } catch (Exception e) {
            logger.error("Failed to get client count", e);
            throw new RuntimeException("Failed to get client count", e);
        }
    }

    public Optional<Client> findClientById(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            return clientDAO.findById(clientId);
        } catch (Exception e) {
            logger.error("Failed to find client by ID", e);
            throw new RuntimeException("Failed to find client by ID", e);
        }
    }

    public void removeClientByRoomNumber(int roomNumber) {
        try {
            Optional<Client> clientOpt = clientDAO.findByRoomNumber(roomNumber);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                clientDAO.delete(client.getId());
            }
        } catch (Exception e) {
            logger.error("Failed to remove client by room", e);
            throw new RuntimeException("Failed to remove client by room", e);
        }
    }
    public void assignClientToRoom(String clientId, int roomNumber) {
        try {
            Optional<Client> clientOpt = clientDAO.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new IllegalArgumentException("Client not found: " + clientId);
            }

            Client client = clientOpt.get();
            client.assignToRoom(roomNumber);
            clientDAO.update(client);

        } catch (SQLException e) {
            logger.error("Failed to assign client to room", e);
            throw new RuntimeException("Failed to assign client to room", e);
        }
    }

    public void vacateClientFromRoom(String clientId) {
        try {
            Optional<Client> clientOpt = clientDAO.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new IllegalArgumentException("Client not found: " + clientId);
            }

            Client client = clientOpt.get();
            client.vacateRoom();
            clientDAO.update(client);

        } catch (SQLException e) {
            logger.error("Failed to vacate client from room", e);
            throw new RuntimeException("Failed to vacate client from room", e);
        }
    }
    public void assignRoomToClient(String clientId, int roomNumber) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            Client client = clientDAO.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));
            client.setRoomNumber(roomNumber);
            clientDAO.update(client);
        } catch (Exception e) {
            logger.error("Failed to assign room to client", e);
            throw new RuntimeException("Failed to assign room to client", e);
        }
    }

    public List<Client> getAllClients(){
        try {
            return clientDAO.findAll();
        } catch (Exception e) {
            logger.error("Failed to get all clients", e);
            throw new RuntimeException("Failed to get all clients", e);
        }
    }

}