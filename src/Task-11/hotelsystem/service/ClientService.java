
package hotelsystem.service;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.interfaceClass.IClientRepository;
import hotelsystem.model.Client;
import hotelsystem.repo.dao.ClientDAO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ClientService implements IClearable {
    @Inject
    private IClientRepository clientRepository;
    @Inject
    private ClientDAO clientDAO;

    public void registerClient(Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        try {
            clientDAO.create(client);
            clientRepository.addClient(client);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to register client", e);
        }    }

    public Optional<Client> findClientByRoomNumber(int roomNumber) {
        try {
            Optional<Client> client = clientRepository.findClientByRoomNumber(roomNumber);
            if (client.isEmpty()) {
                client = clientDAO.findAll().stream()
                        .filter(c -> c.getRoomNumber() == roomNumber)
                        .findFirst();
                client.ifPresent(clientRepository::addClient);
            }
            return client;
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to find client by room", e);
        }
    }

    public int getClientCount(){
        try {
            clientDAO.findAll().forEach(clientRepository::addClient);
            return clientRepository.getClientCount();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get client count", e);
        }    }

    public Optional<Client> findClientById(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            Optional<Client> client = clientRepository.findClientById(clientId);
            if (client.isEmpty()) {
                client = Optional.ofNullable(clientDAO.findById(clientId));
                client.ifPresent(clientRepository::addClient);
            }
            return client;
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to find client by ID", e);
        }
    }

    public void removeClientByRoomNumber(int roomNumber) {
        try {
            clientDAO.findAll().stream()
                    .filter(c -> c.getRoomNumber() == roomNumber)
                    .forEach(c -> {
                        try {
                            clientDAO.delete(c.getId());
                        } catch (DatabaseException e) {
                            throw new RuntimeException(e);
                        }
                    });
            clientRepository.removeClientByRoomNumber(roomNumber);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to remove client by room", e);
        }    }

    public void assignRoomToClient(String clientId, int roomNumber) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        try {
            Client client = findClientById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));

            client.setRoomNumber(roomNumber);
            clientDAO.update(client);
            clientRepository.assignRoomToClient(clientId, roomNumber);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to assign room to client", e);
        }
    }

    public List<Client> getAllClients(){
        try {
            List<Client> clients = clientDAO.findAll();
            clients.forEach(clientRepository::addClient);
            return clientRepository.getAllClients();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get all clients", e);
        }
    }

    @Override
    public void clear() {
        try {
            clientDAO.findAll().forEach(c -> {
                try {
                    clientDAO.delete(c.getId());
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            });
            clientRepository.clearAll();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to clear clients", e);
        }    }
}