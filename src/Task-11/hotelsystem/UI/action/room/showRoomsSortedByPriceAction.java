package hotelsystem.UI.action.room;

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
        logger.info("showRoomsSortedByPriceAction: Начало отображения номеров по цене");
        try {
            System.out.println("\nНомера (сортировка по цене):");
            var rooms = manager.getRooms(SortType.PRICE, false);
            rooms.forEach(r -> System.out.printf("%d - %.2f руб.%n",
                    r.getNumberRoom(), r.getPriceForDay()));

            logger.info("showRoomsSortedByPriceAction: Отображено {} номеров по цене", rooms.size());
        } catch (Exception e) {
            logger.error("showRoomsSortedByPriceAction: Ошибка при отображении номеров по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}