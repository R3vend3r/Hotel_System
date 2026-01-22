package hotelsystem.repository;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.interfaceClass.IClientRepository;
import hotelsystem.model.Client;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientRepository implements IClientRepository {
    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    @Override
    public void addClient(Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        clients.put(client.getId(), client);
    }

    @Override
    public Optional<Client> findClientByRoomNumber(int roomNumber) {
        return clients.values().stream()
                .filter(client -> client.getRoomNumber() == roomNumber)
                .findFirst();
    }

    @Override
    public Optional<Client> findClientById(String clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }
    @Override
    public int getClientCount() {
        return clients.size();
    }

    @Override
    public void removeClientByRoomNumber(int roomNumber) {
        clients.values().removeIf(client -> client.getRoomNumber() == roomNumber);
    }

    @Override
    public void assignRoomToClient(String clientId, int roomNumber) {
        Client client = clients.get(clientId);
        if (client != null) {
            client.setRoomNumber(roomNumber);
        }
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void clearAll() {
        clients.clear();
    }
}