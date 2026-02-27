package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsSortedByStarsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsSortedByStarsAction.class);
    private final RoomController roomController;

    public ShowAvailableRoomsSortedByStarsAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по звездам");
        try {
            System.out.println("\nСвободные номера (по звездам):");
            var rooms = roomController.getRooms(SortType.STARS, true);
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