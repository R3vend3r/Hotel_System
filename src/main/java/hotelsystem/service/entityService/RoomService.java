package hotelsystem.service.entityService;

import hotelsystem.Exception.DaoException;
import hotelsystem.Exception.ServiceException;
import hotelsystem.Utils.HibernateUtil;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.enums.RoomCondition;
import hotelsystem.enums.SortType;
import hotelsystem.model.entity.Room;
import hotelsystem.dao.RoomDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    @Inject
    private RoomDAO roomDAO;

    public void addRoom(Room room) {
        try {
            roomDAO.create(room);
        } catch (DaoException e) {
            logger.error("Failed to add room", e);
            throw new RuntimeException("Failed to add room", e);
        }
    }

    public Optional<Room> findRoom(Integer roomNumber) {
        try {
            return roomDAO.findById(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to find room", e);
            throw new RuntimeException("Failed to find room", e);
        }
    }

    public List<Room> getAllRooms() {
        try {
            return roomDAO.findAll();
        } catch (DaoException e) {
            logger.error("Failed to get all rooms", e);
            throw new RuntimeException("Failed to get all rooms", e);
        }
    }

    public int countAvailableRooms() {
        try {
            return roomDAO.countAvailableRooms();
        } catch (DaoException e) {
            logger.error("Failed to count available rooms", e);
            throw new RuntimeException("Failed to count available rooms", e);
        }
    }

    public boolean isRoomAvailable(Integer roomNumber) {
        try {
            return roomDAO.isRoomAvailable(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to check room availability", e);
            throw new RuntimeException("Failed to check room availability", e);
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

    public void occupyRoom(int roomNumber) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

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

                transaction.commit();
                logger.info("Room {} occupied", roomNumber);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Failed to occupy room", e);
                throw new ServiceException("Failed to occupy room", e);
            }
        }
    }

    public void vacateRoom(int roomNumber) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                Optional<Room> roomOpt = roomDAO.findById(roomNumber);
                if (roomOpt.isEmpty()) {
                    throw new ServiceException("Room not found: " + roomNumber);
                }

                Room room = roomOpt.get();
                room.vacate();
                roomDAO.update(room);

                transaction.commit();
                logger.info("Room {} vacated", roomNumber);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Failed to vacate room", e);
                throw new ServiceException("Failed to vacate room", e);
            }
        }
    }

    public void updateRoomStatus(int roomNumber, RoomCondition status) {
        try {
            roomDAO.updateRoomStatus(roomNumber, status);
        } catch (DaoException e) {
            logger.error("Failed to update room status", e);
            throw new RuntimeException("Failed to update room status", e);
        }
    }

    public void updateRoomPrice(int roomNumber, double newPrice) {
        try {
            roomDAO.updateRoomPrice(roomNumber, newPrice);
        } catch (DaoException e) {
            logger.error("Failed to update room price", e);
            throw new RuntimeException("Failed to update room price", e);
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