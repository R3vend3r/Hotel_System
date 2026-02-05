package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showRoomsSortedByCapacityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showRoomsSortedByCapacityAction.class);
    private final ManagerHotel manager;

    public showRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по вместимости");
        try {
            System.out.println("\nНомера (сортировка по вместимости):");
            var rooms = manager.getRooms(SortType.CAPACITY, false);
            rooms.forEach(r -> System.out.printf("%d - %d чел.%n",
                    r.getNumber(), r.getCapacity()));

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