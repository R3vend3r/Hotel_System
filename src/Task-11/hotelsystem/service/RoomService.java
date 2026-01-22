package hotelsystem.service;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.enums.RoomCondition;
import hotelsystem.enums.SortType;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.model.Client;
import hotelsystem.model.Room;
import hotelsystem.repo.dao.RoomDAO;
import hotelsystem.repository.RoomRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoomService implements IClearable {
    @Inject
    private RoomDAO roomDAO;
    @Inject
    private RoomRepository roomRepository;

    public void addRoom(Room room) {
        try {
            roomDAO.create(room);
            roomRepository.addRoom(room);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to add room", e);
        }
    }

    public Optional<Room> findRoom(int roomNumber) {
        Optional<Room> room = roomRepository.findRoom(roomNumber);
        if (room.isEmpty()) {
            try {
                room = Optional.ofNullable(roomDAO.findById(roomNumber));
                room.ifPresent(roomRepository::addRoom);
            } catch (DatabaseException e) {
                throw new RuntimeException("Failed to find room", e);
            }
        }
        return room;
    }

    public void updateRoom(Room room) {
        try {
            roomDAO.update(room);
            roomRepository.addRoom(room);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to update room", e);
        }
    }

    public List<Room> getAllRooms() {
        try {
            List<Room> rooms = roomDAO.findAll();
            rooms.forEach(roomRepository::addRoom);
            return rooms;
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get all rooms", e);
        }
    }

    public double calculateStayCost(int roomNumber, Date endDate) {
        return findRoom(roomNumber).map(room -> {
            long days = calculateDaysBetween(new Date(), endDate);
            return room.getPriceForDay() * days;
        }).orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    private long calculateDaysBetween(Date start, Date end) {
        long diffInMillis = end.getTime() - start.getTime();
        return (long) Math.ceil(diffInMillis / (1000.0 * 60 * 60 * 24));
    }

    public int countAvailableRooms() {
        return (int) getAllRooms().stream()
                .filter(Room::isAvailable)
                .count();
    }

    public boolean isRoomAvailable(int roomNumber) {
        return findRoom(roomNumber)
                .map(Room::isAvailable)
                .orElse(false);
    }

    public void markRoomOccupied(Room room) {
        room.setAvailable(false);
        updateRoom(room);
    }

    public void clearRoom(int roomNumber) {
        findRoom(roomNumber).ifPresent(room -> {
            room.setAvailable(true);
            room.setRoomCondition(RoomCondition.CLEANING_REQUIRED);
            room.setClientId(null);
            room.setAvailableDate(null);
            updateRoom(room);
        });
    }

    public void assignClientToRoom(int roomNumber, String clientId, Date availableDate) {
        findRoom(roomNumber).ifPresent(room -> {
            room.setClientId(clientId);
            room.setAvailableDate(availableDate);
            room.setAvailable(false);
            updateRoom(room);
        });
    }

    public void addClientToRoomHistory(int roomNumber, Client client) {
        roomRepository.addClientToHistory(roomNumber, client);
    }

    public List<Client> getRoomHistory(int roomNumber) {
        return roomRepository.getRoomHistory(roomNumber);
    }

    public List<Room> getAvailableRoomsByDate(Date date) {
        return getAllRooms().stream()
                .filter(room -> room.isAvailable() ||
                        (room.getAvailableDate() != null && !room.getAvailableDate().after(date)))
                .collect(Collectors.toList());
    }

    public List<Room> getSortedRooms(SortType sortType) {
        return sortRooms(getAllRooms(), sortType);
    }

    public List<Room> getSortedAvailableRooms(SortType sortType) {
        return sortRooms(
                getAllRooms().stream().filter(Room::isAvailable).collect(Collectors.toList()),
                sortType
        );
    }

    private List<Room> sortRooms(List<Room> rooms, SortType sortType) {
        Comparator<Room> comparator = switch (sortType) {
            case CAPACITY -> Comparator.comparingInt(Room::getCapacity);
            case PRICE -> Comparator.comparingDouble(Room::getPriceForDay);
            case STARS -> Comparator.comparingInt(Room::getStars);
            case TYPE -> Comparator.comparing(Room::getType);
            default -> (a, b) -> 0;
        };
        return rooms.stream().sorted(comparator).collect(Collectors.toList());
    }

    public void updateRoomStatus(int roomNumber, RoomCondition status) {
        findRoom(roomNumber).ifPresent(room -> {
            room.setRoomCondition(status);
            updateRoom(room);
        });
    }

    public void updateRoomPrice(int roomNumber, double newPrice) {
        findRoom(roomNumber).ifPresent(room -> {
            room.setPriceForDay(newPrice);
            updateRoom(room);
        });
    }

    @Override
    public void clear() {
        try {
            roomDAO.findAll().forEach(room -> {
                try {
                    roomDAO.delete(room.getNumberRoom());
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            });
            roomRepository.clear();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to clear rooms", e);
        }
    }
}