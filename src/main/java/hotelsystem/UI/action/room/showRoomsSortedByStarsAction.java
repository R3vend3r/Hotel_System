package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showRoomsSortedByStarsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showRoomsSortedByStarsAction.class);
    private final ManagerHotel manager;

    public showRoomsSortedByStarsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по звездам");
        try {
            System.out.println("\nНомера (сортировка по звездам):");
            var rooms = manager.getRooms(SortType.STARS, false);
            rooms.forEach(r -> System.out.printf("%d - %d★%n",
                    r.getNumber(), r.getStars()));

            logger.info("Отображено {} номеров по звездам", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении номеров по звездам: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении номеров по звездам: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}