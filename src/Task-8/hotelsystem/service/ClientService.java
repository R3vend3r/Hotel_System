
package hotelsystem.service;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.interfaceClass.IClientRepository;
import hotelsystem.model.Client;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ClientService implements IClearable {
    @Inject
    private IClientRepository clientRepository;

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

    @Override
    public void clear() {
        clientRepository.clearAll();
    }
}