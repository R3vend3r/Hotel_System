package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
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
        logger.info("showAvailableRoomsSortedByCapacityAction: Начало отображения свободных номеров по вместимости");
        try {
            System.out.println("\nСвободные номера (по вместимости):");
            var rooms = manager.getRooms(SortType.CAPACITY, true);
            rooms.forEach(r -> System.out.printf("%d - %d чел. (%s)%n",
                    r.getNumberRoom(), r.getCapacity(), r.getType()));

            logger.info("showAvailableRoomsSortedByCapacityAction: Отображено {} свободных номеров по вместимости", rooms.size());
        } catch (Exception e) {
            logger.error("showAvailableRoomsSortedByCapacityAction: Ошибка при отображении свободных номеров по вместимости: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}