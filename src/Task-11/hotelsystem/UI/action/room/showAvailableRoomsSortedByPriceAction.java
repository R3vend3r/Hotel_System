package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
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
        logger.info("showAvailableRoomsSortedByPriceAction: Начало отображения свободных номеров по цене");
        try {
            System.out.println("\nСвободные номера (по цене):");
            var rooms = manager.getRooms(SortType.PRICE, true);
            rooms.forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                    r.getNumberRoom(), r.getType(), r.getPriceForDay()));

            logger.info("showAvailableRoomsSortedByPriceAction: Отображено {} свободных номеров по цене", rooms.size());
        } catch (Exception e) {
            logger.error("showAvailableRoomsSortedByPriceAction: Ошибка при отображении свободных номеров по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}