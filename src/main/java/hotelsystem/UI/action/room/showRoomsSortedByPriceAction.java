package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showRoomsSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showRoomsSortedByPriceAction.class);
    private final ManagerHotel manager;

    public showRoomsSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по цене");
        try {
            System.out.println("\nНомера (сортировка по цене):");
            var rooms = manager.getRooms(SortType.PRICE, false);
            rooms.forEach(r -> System.out.printf("%d - %.2f руб.%n",
                    r.getNumber(), r.getPriceForDay()));

            logger.info("Отображено {} номеров по цене", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении номеров по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении номеров по цене: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}