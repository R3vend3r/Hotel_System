package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAvailableRoomsSortedByCapacityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsSortedByCapacityAction.class);
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по вместимости");
        try {
            System.out.println("\nСвободные номера (по вместимости):");
            var rooms = manager.getRooms(SortType.CAPACITY, true);
            rooms.forEach(r -> System.out.printf("%d - %d чел. (%s)%n",
                    r.getNumber(), r.getCapacity(), r.getType()));

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