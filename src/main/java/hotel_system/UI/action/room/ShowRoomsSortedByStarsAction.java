package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowRoomsSortedByStarsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomsSortedByStarsAction.class);
    private final RoomController roomController;

    public ShowRoomsSortedByStarsAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по звездам");
        try {
            System.out.println("\nНомера (сортировка по звездам):");
            var rooms = roomController.getRooms(SortType.STARS, false);
            rooms.forEach(r -> System.out.printf("%d - %d★%n",
                    r.number(), r.stars()));

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