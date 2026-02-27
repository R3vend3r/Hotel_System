package hotel_system.controller;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.Utils.HotelConfig;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Room;
import hotel_system.service.entityService.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelConfig hotelConfig;

    public Optional<Room> findRoom(int roomNumber) {
        return roomService.findRoom(roomNumber);
    }

    public void addRoom(Room room) {
        try {
            roomService.addRoom(room);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении комнаты", e);
            throw new ManagerHotelException("Ошибка при добавлении комнаты: " + e.getMessage(), e);
        }
    }

    public void updateRoomStatus(int number, RoomCondition status) {
        if (hotelConfig.isRoomStatusChangeEnabled()) {
            try {
                roomService.updateRoomStatus(number, status);
            } catch (Exception e) {
                logger.error("Ошибка при обновлении статуса комнаты", e);
                throw new ManagerHotelException("Ошибка при обновлении статуса комнаты: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalStateException("Изменение статуса комнаты запрещено конфигурацией");
        }
    }

    public void updateRoomPrice(int number, double newPrice) {
        try {
            roomService.updateRoomPrice(number, newPrice);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении цены комнаты", e);
            throw new ManagerHotelException("Ошибка при обновлении цены комнаты: " + e.getMessage(), e);
        }
    }

    public List<Room> getRooms(SortType sortType, boolean onlyAvailable) {
        return onlyAvailable
                ? roomService.getSortedAvailableRooms(sortType)
                : roomService.getSortedRooms(sortType);
    }

    public List<Room> getAvailableRoomsByDate(Date date) {
        return roomService.getAvailableRoomsByDate(date);
    }

    public int getAvailableRoomsCount() {
        return roomService.countAvailableRooms();
    }

    public boolean isRoomAvailable(int number) {
        return roomService.isRoomAvailable(number);
    }

}
