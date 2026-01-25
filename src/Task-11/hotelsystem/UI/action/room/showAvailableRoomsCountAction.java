package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAvailableRoomsCountAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsCountAction.class);
    private final ManagerHotel manager;

    public showAvailableRoomsCountAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showAvailableRoomsCountAction: Начало подсчета свободных номеров");
        try {
            int availableRoomsCount = manager.getAvailableRoomsCount();
            System.out.printf("\nСвободных номеров: %d%n", availableRoomsCount);
            logger.info("showAvailableRoomsCountAction: Найдено {} свободных номеров", availableRoomsCount);
        } catch (Exception e) {
            logger.error("showAvailableRoomsCountAction: Ошибка при подсчете свободных номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}