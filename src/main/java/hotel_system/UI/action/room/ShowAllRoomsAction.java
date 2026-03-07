package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAllRoomsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllRoomsAction.class);
    private final RoomController roomController;

    public ShowAllRoomsAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения всех номеров");
        try {
            System.out.println("\nВсе номера:");
            var rooms = roomController.getRooms(SortType.NONE, false);
            rooms.forEach(System.out::println);

            System.out.println("Всего: " + rooms.size());
            logger.info("Отображено {} всех номеров", rooms.size());

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении всех номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении всех номеров: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}