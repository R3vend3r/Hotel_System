package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsCountAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsCountAction.class);
    private final RoomController roomController;

    public ShowAvailableRoomsCountAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало подсчета свободных номеров");
        try {
            int availableRoomsCount = roomController.getAvailableRoomsCount();
            System.out.printf("\nСвободных номеров: %d%n", availableRoomsCount);
            logger.info("Найдено {} свободных номеров", availableRoomsCount);
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при подсчете свободных номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при подсчете свободных номеров: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}