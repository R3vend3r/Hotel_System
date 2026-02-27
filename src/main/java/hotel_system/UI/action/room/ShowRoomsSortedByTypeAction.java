package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowRoomsSortedByTypeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomsSortedByTypeAction.class);
    private final RoomController roomController;

    public ShowRoomsSortedByTypeAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по типу");
        try {
            System.out.println("\nНомера (сортировка по типу):");
            var rooms = roomController.getRooms(SortType.TYPE, false);
            rooms.forEach(System.out::println);

            logger.info("Отображено {} номеров по типу", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении номеров по типу: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении номеров по типу: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}