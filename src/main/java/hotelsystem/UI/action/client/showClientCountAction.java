package hotelsystem.UI.action.client;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showClientCountAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientCountAction.class);
    private final ManagerHotel manager;

    public showClientCountAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало подсчета количества клиентов");
        try {
            int count = manager.getClientCount();
            System.out.printf("\nОбслужено клиентов: %d%n", count);
            logger.info("Обслужено {} клиентов", count);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при подсчете клиентов: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при подсчете клиентов: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}