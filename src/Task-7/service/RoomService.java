package service;

import enums.RoomCondition;
import enums.SortType;
import interfaceClass.*;
import model.Client;
import model.Room;


import java.util.*;


public class RoomService {
    private final IRoomRepository roomRepository;

    public RoomService(IRoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "RoomRepository cannot be null");
    }

    public void addRoom(Room room) {
        roomRepository.addRoom(room);
    }

    public Optional<Room> findRoom(int roomNumber) {
        return roomRepository.findRoomByNumber(roomNumber);
    }

    public void markRoomOccupied(Room room) {
        Objects.requireNonNull(room, "Room cannot be null");
        roomRepository.markRoomOccupied(room);
    }

    public void clearRoom(int roomNumber) {
        roomRepository.clearRoom(roomNumber);
    }

    public void updateRoomStatus(int number, RoomCondition status) {
        Objects.requireNonNull(status, "Status cannot be null");
        roomRepository.changeRoomCondition(number, status);
    }

    public void updateRoomPrice(int number, double newPrice) {
        roomRepository.changeRoomPrice(number, newPrice);
    }

    public void assignClientToRoom(int roomNumber, String clientId, Date availableDate) {
        Objects.requireNonNull(clientId, "Client ID cannot be null");
        Objects.requireNonNull(availableDate, "Available date cannot be null");
        roomRepository.assignClientToRoom(roomNumber, clientId, availableDate);
    }

    public void addClientToRoomHistory(int roomNumber, Client client) {
        roomRepository.addClientToRoomHistory(roomNumber, client);
    }

    public Optional<String> getAssignedClient(int roomNumber) {
        return Optional.ofNullable(roomRepository.getAssignedClientId(roomNumber));
    }

    public Map<Integer, Room> getRoomsByCondition(RoomCondition condition) {
        Objects.requireNonNull(condition, "Condition cannot be null");
        return roomRepository.getRoomsByCondition(condition);
    }

    public Map<Integer, Room> getAvailableRoomsByDate(Date date) {
        return roomRepository.getAvailableRoomsByDate(date);
    }

    public Map<Integer, Room> getSortedRooms(SortType sortType) {
        Objects.requireNonNull(sortType, "Sort type cannot be null");
        return roomRepository.getSortedRooms(sortType);
    }

    public List<Client> getRoomHistory(int roomNumber) {
        return roomRepository.getRoomHistory(roomNumber);
    }

    public Map<Integer, Room> getSortedAvailableRooms(SortType sortType) {
        Objects.requireNonNull(sortType, "Sort type cannot be null");
        return roomRepository.getSortedAvailableRooms(sortType);
    }

    public boolean isRoomAvailable(int roomNumber) {
        return roomRepository.isRoomAvailable(roomNumber);
    }

    public int countAvailableRooms() {
        return roomRepository.countAvailableRooms();
    }

    public double calculateStayCost(int roomNumber, Date endDate) {
        Objects.requireNonNull(endDate, "End date cannot be null");
        return roomRepository.calculateStayCost(roomNumber, endDate);
    }

    public String getRoomDetails(int roomNumber) {
        return roomRepository.getRoomDetails(roomNumber);
    }

    public void clearAll() {
        roomRepository.clearAll();
    }
}