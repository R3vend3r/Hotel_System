package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowRoomsSortedByCapacityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomsSortedByCapacityAction.class);
    private final RoomController roomController;

    public ShowRoomsSortedByCapacityAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по вместимости");
        try {
            System.out.println("\nНомера (сортировка по вместимости):");
            var rooms = roomController.getRooms(SortType.CAPACITY, false);
            rooms.forEach(r -> System.out.printf("%d - %d чел.%n",
                    r.number(), r.capacity()));

            logger.info("Отображено {} номеров по вместимости", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}