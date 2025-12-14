package interfaceClass;

import model.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {
    void addClient(Client client);
    boolean clientExists(String clientId);
    Optional<Client> findClientByRoomNumber(int roomNumber);
    Optional<Client> findClientById(String clientId);
    void removeClientByRoomNumber(int roomNumber);
    void assignRoomToClient(String clientId, int roomNumber);
    int getClientCount();
    List<Client> getAllClients();
    void clearAll();
}