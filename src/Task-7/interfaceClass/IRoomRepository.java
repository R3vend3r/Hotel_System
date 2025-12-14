package interfaceClass;

import enums.RoomCondition;
import enums.SortType;
import model.Client;
import model.Room;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRoomRepository {
    void addRoom(Room room);
    Optional<Room> findRoomByNumber(int number);

    void markRoomOccupied(Room room);
    void clearRoom(int roomNumber);
    void changeRoomCondition(int number, RoomCondition newStatus);
    void changeRoomPrice(int number, double newPrice);

    void assignClientToRoom(int roomNumber, String clientId, Date availableDate);
    String getAssignedClientId(int roomNumber);

    void addClientToRoomHistory(int roomNumber, Client client);
    List<Client> getRoomHistory(int roomNumber);

    Map<Integer, Room> getAllRooms();
    Map<Integer, Room> getAvailableRooms();
    Map<Integer, Room> getRoomsByCondition(RoomCondition condition);
    Map<Integer, Room> getAvailableRoomsByDate(Date date);

    Map<Integer, Room> getSortedRooms(SortType sortType);
    Map<Integer, Room> getSortedAvailableRooms(SortType sortType);

    boolean isRoomAvailable(int roomNumber);
    int countAvailableRooms();
    double calculateStayCost(int number, Date endDate);
    String getRoomDetails(int roomNumber);
    void clearAll();
}