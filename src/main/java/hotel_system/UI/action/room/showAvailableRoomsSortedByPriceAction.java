package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAvailableRoomsSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsSortedByPriceAction.class);
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по цене");
        try {
            System.out.println("\nСвободные номера (по цене):");
            var rooms = manager.getRooms(SortType.PRICE, true);
            rooms.forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                    r.getNumber(), r.getType(), r.getPriceForDay()));

            logger.info("Отображено {} свободных номеров по цене", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров по цене: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}