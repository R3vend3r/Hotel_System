package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.ClientRequest;
import hotel_system.dto.ClientResponse;
import hotel_system.model.entity.Client;
import hotel_system.dao.ClientDAO;
import hotel_system.model.mapper.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientDAO clientDAO;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientDAO clientDAO, ClientMapper clientMapper) {
        this.clientDAO = clientDAO;
        this.clientMapper = clientMapper;
    }

    @Transactional
    public void registerClient(ClientRequest request) {
        Objects.requireNonNull(request, "Client cannot be null");
        try {
            Client client = clientMapper.toEntity(request);
            clientDAO.create(client);
        } catch (DaoException e) {
            logger.error("Failed to register client", e);
            throw new ServiceException("Failed to register client", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ClientResponse> findClientByRoomNumber(Integer roomNumber) {
        try {
            return clientDAO.findByRoomNumber(roomNumber)
                    .map(clientMapper::toResponse);
        } catch (DaoException e) {
            logger.error("Failed to find client by room", e);
            throw new ServiceException("Failed to find client by room", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ClientResponse> findByNameAndSurname(String name, String lastName) {
        try {
            return clientDAO.findByNameAndSurname(name, lastName)
                    .map(clientMapper::toResponse);
        } catch (DaoException e) {
            logger.error("Failed to find client by room", e);
            throw new ServiceException("Failed to find client by room", e);
        }
    }

    @Transactional(readOnly = true)
    public int getClientCount(){
        try {
            return (int) clientDAO.count();
        } catch (DaoException e) {
            logger.error("Failed to get client count", e);
            throw new ServiceException("Failed to get client count", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ClientResponse> findClientById(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            return clientDAO.findById(clientId)
                    .map(clientMapper::toResponse);
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
                throw new ServiceException("Client not found: " + clientId);
            }

            Client client = clientOpt.get();
            client.assignToRoom(roomNumber);
            clientDAO.update(client);

            logger.info("Client {} assigned to room {}", clientId, roomNumber);
        } catch (Exception e) {
            logger.error("Failed to assign client to room", e);
            throw new ServiceException("Failed to assign client to room", e);
        }
    }
    @Transactional
    public void vacateClientFromRoom(String clientId) {
        try {
            Optional<Client> clientOpt = clientDAO.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new ServiceException("Client not found: " + clientId);
            }
            Client client = clientOpt.get();
            if (client.getRoomNumber() == null) {
                logger.warn("Client {} is not assigned to any room", clientId);
                return;
            }
            client.vacateRoom();
            clientDAO.update(client);

            logger.info("Client {} vacated from room", clientId);
        } catch (Exception e) {
            logger.error("Failed to vacate client from room", e);
            throw new ServiceException("Failed to vacate client from room", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> getAllClients(){
        try {
            return clientDAO.findAll().stream()
                    .map(clientMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Failed to get all clients", e);
            throw new ServiceException("Failed to get all clients", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Client> findClientEntityById(String clientId) {
        try {
            return clientDAO.findById(clientId);
        } catch (DaoException e) {
            logger.error("Failed to find client entity by id: {}", clientId, e);
            throw new ServiceException("Failed to find client", e);
        }
    }
}