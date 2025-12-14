package model;

import Utils.HotelConfig;
import enums.RoomCondition;
import enums.SortType;
import interfaceClass.IRoomRepository;

import java.util.*;
import java.util.stream.Collectors;

public class RoomRepository implements IRoomRepository {
    private final Map<Integer, Room> rooms = new HashMap<>();
    private final Map<Integer, Queue<Client>> roomHistory = new HashMap<>();

    @Override
    public void addRoom(Room room) {
        validateRoom(room);
        checkRoomDoesNotExist(room.getNumberRoom());
        rooms.put(room.getNumberRoom(), room);
    }

    private void validateRoom(Room room) {
        Objects.requireNonNull(room, "Room cannot be null");
    }

    private void checkRoomDoesNotExist(int roomNumber) {
        if (rooms.containsKey(roomNumber)) {
            throw new IllegalArgumentException("Room " + roomNumber + " already exists");
        }
    }

    @Override
    public Optional<Room> findRoomByNumber(int number) {
        return Optional.ofNullable(rooms.get(number));
    }

    @Override
    public void markRoomOccupied(Room room) {
        Objects.requireNonNull(room, "Room cannot be null");
        room.changeAvailability();
    }

    @Override
    public void clearRoom(int roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        room.clearRoom();
    }

    private Room getRoomOrThrow(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " not found");
        }
        return room;
    }

    @Override
    public void changeRoomCondition(int number, RoomCondition newStatus) {
        Objects.requireNonNull(newStatus, "Status cannot be null");

        Room room = rooms.get(number);
        if (room != null) {
            room.setRoomCondition(newStatus);
        }
    }

    @Override
    public void changeRoomPrice(int number, double newPrice) {
        Room room = rooms.get(number);
        if (room != null) {
            room.setPriceForDay(newPrice);
        }
    }

    @Override
    public void assignClientToRoom(int roomNumber, String clientId, Date availableDate) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        Objects.requireNonNull(availableDate, "Available date cannot be null");

        Room room = getRoomOrThrow(roomNumber);
        room.setClientIdAndDateAvailable(clientId, availableDate);
    }

    @Override
    public String getAssignedClientId(int roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        return room.getClientId();
    }

    @Override
    public Map<Integer, Room> getAllRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    @Override
    public Map<Integer, Room> getAvailableRooms() {
        return filterAndSortRooms(SortType.NONE, true);
    }

    @Override
    public Map<Integer, Room> getRoomsByCondition(RoomCondition condition) {
        Objects.requireNonNull(condition, "Condition cannot be null");

        return rooms.values().stream()
                .filter(room -> room.getRoomCondition() == condition)
                .collect(Collectors.toMap(
                        Room::getNumberRoom,
                        room -> room,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<Integer, Room> getAvailableRoomsByDate(Date date) {
        Objects.requireNonNull(date, "Date cannot be null");

        return rooms.values().stream()
                .filter(room -> isRoomAvailableByDate(room, date))
                .collect(Collectors.toMap(
                        Room::getNumberRoom,
                        room -> room,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private boolean isRoomAvailableByDate(Room room, Date date) {
        Date availableDate = room.getAvailableDate();
        return room.isAvailable() || (availableDate != null && !availableDate.after(date));
    }

    @Override
    public Map<Integer, Room> getSortedRooms(SortType sortType) {
        return filterAndSortRooms(sortType, false);
    }

    @Override
    public Map<Integer, Room> getSortedAvailableRooms(SortType sortType) {
        return filterAndSortRooms(sortType, true);
    }

    private Map<Integer, Room> filterAndSortRooms(SortType sortType, boolean onlyAvailable) {
        Objects.requireNonNull(sortType, "Sort type cannot be null");

        Comparator<Room> comparator = getRoomComparator(sortType);

        return rooms.values().stream()
                .filter(room -> !onlyAvailable || room.isAvailable())
                .sorted(comparator)
                .collect(Collectors.toMap(
                        Room::getNumberRoom,
                        room -> room,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private Comparator<Room> getRoomComparator(SortType sortType) {
        return switch (sortType) {
            case CAPACITY -> Comparator.comparingInt(Room::getCapacity);
            case PRICE -> Comparator.comparingDouble(Room::getPriceForDay);
            case STARS -> Comparator.comparingInt(Room::getStars);
            case TYPE -> Comparator.comparing(Room::getType);
            case NONE -> (a, b) -> 0;
            default -> throw new IllegalArgumentException("Unknown sort type: " + sortType);
        };
    }

    @Override
    public boolean isRoomAvailable(int roomNumber) {
        Room room = rooms.get(roomNumber);
        return room != null && room.isAvailable();
    }

    @Override
    public int countAvailableRooms() {
        return (int) rooms.values().stream()
                .filter(Room::isAvailable)
                .count();
    }

    @Override
    public void addClientToRoomHistory(int roomNumber, Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " not found");
        }

        roomHistory.computeIfAbsent(roomNumber, k -> new LinkedList<>()).add(client);

        Queue<Client> history = roomHistory.get(roomNumber);
        while (history.size() > HotelConfig.getMaxHistoryEntries()) {
            history.poll();
        }
    }

    @Override
    public double calculateStayCost(int roomNumber, Date endDate) {
        Objects.requireNonNull(endDate, "End date cannot be null");

        Room room = getRoomOrThrow(roomNumber);
        validateEndDate(endDate);

        return room.getPriceForDay() * calculateDaysBetween(new Date(), endDate);
    }

    private void validateEndDate(Date endDate) {
        Date now = new Date();
        if (endDate.before(now)) {
            throw new IllegalArgumentException("End date cannot be in the past");
        }
    }

    @Override
    public List<Client> getRoomHistory(int roomNumber) {
        Queue<Client> history = roomHistory.get(roomNumber);
        return history != null ? new ArrayList<>(history) : Collections.emptyList();
    }

    private double calculateDaysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return Math.ceil((double) diff / (1000 * 60 * 60 * 24));
    }

    @Override
    public String getRoomDetails(int roomNumber) {
        Room room = rooms.get(roomNumber);
        return room != null ? room.toString() : "Room not found";
    }

    @Override
    public void clearAll() {
        rooms.clear();
    }
}