package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAllAvailableRoomsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllAvailableRoomsAction.class);
    private final RoomController roomController;

    public ShowAllAvailableRoomsAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров");
        try {
            System.out.println("\n=== СПИСОК СВОБОДНЫХ НОМЕРОВ ===");
            var rooms = roomController.getRooms(SortType.NONE, true);

            System.out.println("Доступно номеров: " + rooms.size());
            System.out.println("-".repeat(30));

            if (rooms.isEmpty()) {
                System.out.println("Свободных номеров нет");
                logger.info("Свободных номеров нет");
            } else {
                rooms.forEach(System.out::println);
                logger.info("Отображено {} свободных номеров", rooms.size());
            }

            System.out.println("-".repeat(30));

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}