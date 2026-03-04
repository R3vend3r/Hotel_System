package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsSortedByCapacityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsSortedByCapacityAction.class);
    private final RoomController roomController;

    public ShowAvailableRoomsSortedByCapacityAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по вместимости");
        try {
            System.out.println("\nСвободные номера (по вместимости):");
            var rooms = roomController.getRooms(SortType.CAPACITY, true);
            rooms.forEach(r -> System.out.printf("%d - %d чел. (%s)%n",
                    r.number(), r.capacity(), r.type()));

            logger.info("Отображено {} свободных номеров по вместимости", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}