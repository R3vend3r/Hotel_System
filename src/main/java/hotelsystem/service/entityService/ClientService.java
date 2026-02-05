package hotelsystem.service.entityService;

import hotelsystem.Exception.DaoException;
import hotelsystem.Exception.ServiceException;
import hotelsystem.Utils.HibernateUtil;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.model.entity.Client;
import hotelsystem.dao.ClientDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void assignClientToRoom(String clientId, Integer roomNumber) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                Optional<Client> clientOpt = clientDAO.findById(clientId);
                if (clientOpt.isEmpty()) {
                    throw new ServiceException("Client not found: " + clientId);
                }

                Client client = clientOpt.get();
                client.assignToRoom(roomNumber);
                clientDAO.update(client);

                transaction.commit();
                logger.info("Client {} assigned to room {}", clientId, roomNumber);
            } catch (DaoException e) {
                transaction.rollback();
                logger.error("Failed to assign client to room", e);
                throw new ServiceException("Failed to assign client to room", e);
            }
        }
    }

    public void vacateClientFromRoom(String clientId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                Optional<Client> clientOpt = clientDAO.findById(clientId);
                if (clientOpt.isEmpty()) {
                    throw new ServiceException("Client not found: " + clientId);
                }

                Client client = clientOpt.get();
                client.vacateRoom();
                clientDAO.update(client);

                transaction.commit();
                logger.info("Client {} vacated from room", clientId);
            } catch (DaoException e) {
                transaction.rollback();
                logger.error("Failed to vacate client from room", e);
                throw new ServiceException("Failed to vacate client from room", e);
            }
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