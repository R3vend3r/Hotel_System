package hotelsystem.repository;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.interfaceClass.IRoomRepository;
import hotelsystem.model.Client;
import hotelsystem.model.Room;
import java.util.*;

@Component
public class RoomRepository implements IRoomRepository {
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final Map<Integer, Queue<Client>> roomHistory = new HashMap<>();

    @Override
    public void addRoom(Room room) {
        rooms.put(room.getNumberRoom(), room);
    }

    @Override
    public Optional<Room> findRoom(int number) {
        return Optional.ofNullable(rooms.get(number));
    }

    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public void addClientToHistory(int roomNumber, Client client) {
        roomHistory.computeIfAbsent(roomNumber, k -> new LinkedList<>()).add(client);
    }

    @Override
    public List<Client> getRoomHistory(int roomNumber) {
        Queue<Client> history = roomHistory.get(roomNumber);
        return history != null ? new ArrayList<>(history) : Collections.emptyList();
    }

    @Override
    public void clear() {
        rooms.clear();
        roomHistory.clear();
    }
}