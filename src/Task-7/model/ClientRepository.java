package model;

import interfaceClass.IClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientRepository implements IClientRepository {
    private final List<Client> clients;

    public ClientRepository() {
        this.clients = new CopyOnWriteArrayList<>(); // Потокобезопасная коллекция
    }

    @Override
    public void addClient(Client client) throws IllegalArgumentException {
        Objects.requireNonNull(client, "Client cannot be null");

        if (clientExists(client.getId())) {
            throw new IllegalArgumentException("Client with ID " + client.getId() + " already exists");
        }

        clients.add(client);
    }

    @Override
    public boolean clientExists(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        return clients.stream().anyMatch(c -> c.getId().equals(clientId));
    }

    @Override
    public Optional<Client> findClientByRoomNumber(int roomNumber) {
        return clients.stream()
                .filter(client -> client.getRoomNumber() == roomNumber)
                .findFirst();
    }

    @Override
    public Optional<Client> findClientById(String clientId) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        return clients.stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst();
    }
    @Override
    public int getClientCount() {
        return clients.size();
    }

    @Override
    public void removeClientByRoomNumber(int roomNumber) {
        clients.removeIf(client -> client.getRoomNumber() == roomNumber);
    }

    @Override
    public void assignRoomToClient(String clientId, int roomNumber) throws IllegalArgumentException{
        Objects.requireNonNull(clientId, "Client ID cannot be null");

        findClientById(clientId).ifPresentOrElse(
                client -> client.setRoomNumber(roomNumber),
                () -> { throw new IllegalArgumentException("Client not found"); }
        );
    }

    @Override
    public List<Client> getAllClients() {
        return Collections.unmodifiableList(clients);
    }

    @Override
    public void clearAll() {
        clients.clear();
    }
}