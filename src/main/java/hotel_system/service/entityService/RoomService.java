package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Room;
import hotel_system.dao.RoomDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomDAO roomDAO;

    @Autowired
    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Transactional
    public void addRoom(Room room) {
        try {
            roomDAO.create(room);
        } catch (DaoException e) {
            logger.error("Failed to add room", e);
            throw new ServiceException("Failed to add room", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Room> findRoom(Integer roomNumber) {
        try {
            return roomDAO.findById(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to find room", e);
            throw new ServiceException("Failed to find room", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        try {
            return roomDAO.findAll();
        } catch (DaoException e) {
            logger.error("Failed to get all rooms", e);
            throw new ServiceException("Failed to get all rooms", e);
        }
    }

    @Transactional(readOnly = true)
    public int countAvailableRooms() {
        try {
            return roomDAO.countAvailableRooms();
        } catch (DaoException e) {
            logger.error("Failed to count available rooms", e);
            throw new ServiceException("Failed to count available rooms", e);
        }
    }

    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Integer roomNumber) {
        try {
            return roomDAO.isRoomAvailable(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to check room availability", e);
            throw new ServiceException("Failed to check room availability", e);
        }
    }

    public List<Room> getAvailableRoomsByDate(Date date) {
        List<Room> allRooms = getAllRooms();
        return allRooms.stream()
                .filter(room -> room.isAvailable() ||
                        (room.getAvailableDate() != null && !room.getAvailableDate().after(date)))
                .collect(Collectors.toList());
    }

    public List<Room> getSortedRooms(SortType sortType) {
        List<Room> rooms = getAllRooms();
        return sortRooms(rooms, sortType);
    }

    public List<Room> getSortedAvailableRooms(SortType sortType) {
        List<Room> rooms = getAllRooms();
        List<Room> availableRooms = rooms.stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
        return sortRooms(availableRooms, sortType);
    }

    @Transactional
    public void occupyRoom(int roomNumber) {
        try {
            Optional<Room> roomOpt = roomDAO.findById(roomNumber);
            if (roomOpt.isEmpty()) {
                throw new ServiceException("Room not found: " + roomNumber);
            }
            Room room = roomOpt.get();
            if (!room.isAvailable()) {
                throw new ServiceException("Room " + roomNumber + " is already occupied");
            }
            room.occupy();
            roomDAO.update(room);
            logger.info("Room {} occupied", roomNumber);
        } catch (DaoException e) {
            logger.error("Database error while occupying room {}", roomNumber, e);
            throw new ServiceException("Failed to occupy room due to database error", e);
        }
    }

    @Transactional
    public void vacateRoom(int roomNumber) {
        try {
            Optional<Room> roomOpt = roomDAO.findById(roomNumber);
            if (roomOpt.isEmpty()) {
                throw new ServiceException("Room not found: " + roomNumber);
            }
            Room room = roomOpt.get();
            room.vacate();
            roomDAO.update(room);

            logger.info("Room {} vacated", roomNumber);

        } catch (ServiceException e) {
            logger.debug("Business error while vacating room {}: {}", roomNumber, e.getMessage());
            throw e;
        } catch (DaoException e) {
            logger.error("Database error while vacating room {}", roomNumber, e);
            throw new ServiceException("Failed to vacate room due to database error", e);
        } catch (Exception e) {
            logger.error("Unexpected error while vacating room {}", roomNumber, e);
            throw new ServiceException("Unexpected error while vacating room", e);
        }
    }

    @Transactional
    public void updateRoomStatus(int roomNumber, RoomCondition status) {
        try {
            roomDAO.updateRoomStatus(roomNumber, status);
        } catch (DaoException e) {
            logger.error("Failed to update room status", e);
            throw new ServiceException("Failed to update room status", e);
        }
    }

    @Transactional
    public void updateRoomPrice(int roomNumber, double newPrice) {
        try {
            roomDAO.updateRoomPrice(roomNumber, newPrice);
        } catch (DaoException e) {
            logger.error("Failed to update room price", e);
            throw new ServiceException("Failed to update room price", e);
        }
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
}