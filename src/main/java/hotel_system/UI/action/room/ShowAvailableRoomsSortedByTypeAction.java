package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsSortedByTypeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsSortedByTypeAction.class);
    private final ManagerHotel manager;

    public ShowAvailableRoomsSortedByTypeAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по типу");
        try {
            System.out.println("\nСвободные номера (по типу):");
            var rooms = manager.getRooms(SortType.TYPE, true);
            rooms.forEach(System.out::println);

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