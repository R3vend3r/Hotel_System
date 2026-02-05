package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAvailableRoomsSortedByTypeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsSortedByTypeAction.class);
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByTypeAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по типу");
        try {
            System.out.println("\nСвободные номера (по типу):");
            var rooms = manager.getRooms(SortType.TYPE, true);
            rooms.forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                    r.getNumber(), r.getType(), r.getPriceForDay()));

            logger.info("Отображено {} свободных номеров по типу", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров по типу: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров по типу: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}