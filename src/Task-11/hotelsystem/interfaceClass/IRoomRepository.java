package hotelsystem.interfaceClass;

import hotelsystem.model.*;

import java.util.List;
import java.util.Optional;

public interface IRoomRepository {
    void addRoom(Room room);
    Optional<Room> findRoom(int number);
    List<Room> getAllRooms();
    void addClientToHistory(int roomNumber, Client client);
    List<Client> getRoomHistory(int roomNumber);
    void clear();
}