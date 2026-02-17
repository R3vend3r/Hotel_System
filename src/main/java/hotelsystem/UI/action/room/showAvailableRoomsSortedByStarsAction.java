package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAvailableRoomsSortedByStarsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsSortedByStarsAction.class);
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByStarsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по звездам");
        try {
            System.out.println("\nСвободные номера (по звездам):");
            var rooms = manager.getRooms(SortType.STARS, true);
            rooms.forEach(r -> System.out.printf("%d - %d★ (%s)%n",
                    r.getNumber(), r.getStars(), r.getType()));

            logger.info("Отображено {} свободных номеров по звездам", rooms.size());
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров по звездам: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров по звездам: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}