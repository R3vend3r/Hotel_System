package hotelsystem.UI.action.room;

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
        logger.info("showAvailableRoomsSortedByStarsAction: Начало отображения свободных номеров по звездам");
        try {
            System.out.println("\nСвободные номера (по звездам):");
            var rooms = manager.getRooms(SortType.STARS, true);
            rooms.forEach(r -> System.out.printf("%d - %d★ (%s)%n",
                    r.getNumberRoom(), r.getStars(), r.getType()));

            logger.info("showAvailableRoomsSortedByStarsAction: Отображено {} свободных номеров по звездам", rooms.size());
        } catch (Exception e) {
            logger.error("showAvailableRoomsSortedByStarsAction: Ошибка при отображении свободных номеров по звездам: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}