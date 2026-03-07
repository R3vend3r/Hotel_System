package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsSortedByPriceAction.class);
    private final RoomController roomController;

    public ShowAvailableRoomsSortedByPriceAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров по цене");
        try {
            System.out.println("\nСвободные номера (по цене):");
            var rooms = roomController.getRooms(SortType.PRICE, true);
            rooms.forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                    r.number(), r.type(), r.price()));

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