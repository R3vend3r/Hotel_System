
package service;

import interfaceClass.*;
import model.Client;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientService {
    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = Objects.requireNonNull(clientRepository,
                "ClientRepository cannot be null");
    }

    public void registerClient(Client client) {
        clientRepository.addClient(client);
    }

    public Optional<Client> findClientByRoomNumber(int roomNumber) {
        return clientRepository.findClientByRoomNumber(roomNumber);
    }

    public int getClientCount(){
        return clientRepository.getClientCount();
    }

    public Optional<Client> findClientById(String clientId) {
        return clientRepository.findClientById(clientId);
    }

    public void removeClientByRoomNumber(int roomNumber) {
        clientRepository.removeClientByRoomNumber(roomNumber);
    }

    public void assignRoomToClient(String clientId, int roomNumber) {
        clientRepository.assignRoomToClient(clientId, roomNumber);
    }

    public List<Client> getAllClients(){
        return clientRepository.getAllClients();
    }

    public void clearAll() {
        clientRepository.clearAll();
    }
}