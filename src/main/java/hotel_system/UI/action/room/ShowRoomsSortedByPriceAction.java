package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowRoomsSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomsSortedByPriceAction.class);
    private final RoomController roomController;

    public ShowRoomsSortedByPriceAction(RoomController  roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения номеров по цене");
        try {
            System.out.println("\nНомера (сортировка по цене):");
            var rooms = roomController.getRooms(SortType.PRICE, false);
            rooms.forEach(r -> System.out.printf("%d - %.2f руб.%n",
                    r.number(), r.price()));

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