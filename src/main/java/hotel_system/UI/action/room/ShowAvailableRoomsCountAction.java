package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAvailableRoomsCountAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsCountAction.class);
    private final ManagerHotel manager;

    public ShowAvailableRoomsCountAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало подсчета свободных номеров");
        try {
            int availableRoomsCount = manager.getAvailableRoomsCount();
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