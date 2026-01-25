package hotelsystem.UI.action.room;

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
        logger.info("showRoomsSortedByCapacityAction: Начало отображения номеров по вместимости");
        try {
            System.out.println("\nНомера (сортировка по вместимости):");
            var rooms = manager.getRooms(SortType.CAPACITY, false);
            rooms.forEach(r -> System.out.printf("%d - %d чел.%n",
                    r.getNumberRoom(), r.getCapacity()));

            logger.info("showRoomsSortedByCapacityAction: Отображено {} номеров по вместимости", rooms.size());
        } catch (Exception e) {
            logger.error("showRoomsSortedByCapacityAction: Ошибка при отображении номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}