package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.model.entity.Client;
import hotel_system.dao.ClientDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientDAO clientDAO;

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public void registerClient(Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        try {
            clientDAO.create(client);
        } catch (DaoException e) {
            logger.error("Failed to register client", e);
            throw new ServiceException("Failed to register client", e);
        }
    }

    public Optional<Client> findClientByRoomNumber(Integer roomNumber) {
        try {
            return clientDAO.findByRoomNumber(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to find client by room", e);
            throw new ServiceException("Failed to find client by room", e);
        }
    }

    public int getClientCount(){
        try {
            List<Client> clients = clientDAO.findAll();
            return clients.size();
        } catch (DaoException e) {
            logger.error("Failed to get client count", e);
            throw new ServiceException("Failed to get client count", e);
        }
    }

    public Optional<Client> findClientById(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            return clientDAO.findById(clientId);
        } catch (DaoException e) {
            logger.error("Failed to find client by ID", e);
            throw new ServiceException("Failed to find client by ID", e);
        }
    }



    @Transactional
    public void assignClientToRoom(String clientId, Integer roomNumber) {
        try {
            Optional<Client> clientOpt = clientDAO.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new RuntimeException("Client not found: " + clientId);
            }

            Client client = clientOpt.get();
            client.assignToRoom(roomNumber);
            clientDAO.update(client);

            logger.info("Client {} assigned to room {}", clientId, roomNumber);
        } catch (Exception e) {
            logger.error("Failed to assign client to room", e);
            throw new RuntimeException("Failed to assign client to room", e);
        }
    }

    @Transactional
    public void vacateClientFromRoom(String clientId) {
        try {
            Optional<Client> clientOpt = clientDAO.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new RuntimeException("Client not found: " + clientId);
            }

            Client client = clientOpt.get();
            client.vacateRoom();
            clientDAO.update(client);

            logger.info("Client {} vacated from room", clientId);
        } catch (Exception e) {
            logger.error("Failed to vacate client from room", e);
            throw new RuntimeException("Failed to vacate client from room", e);
        }
    }

    public List<Client> getAllClients(){
        try {
            return clientDAO.findAll();
        } catch (DaoException e) {
            logger.error("Failed to get all clients", e);
            throw new ServiceException("Failed to get all clients", e);
        }
    }

}