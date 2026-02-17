package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showRoomsSortedByTypeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showRoomsSortedByTypeAction.class);
    private final ManagerHotel manager;

    public showRoomsSortedByTypeAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по типу");
        try {
            System.out.println("\nНомера (сортировка по типу):");
            var rooms = manager.getRooms(SortType.TYPE, false);
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