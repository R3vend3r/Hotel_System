package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowClientCountAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientCountAction.class);
    private final ManagerHotel manager;

    public ShowClientCountAction(ManagerHotel manager) {
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