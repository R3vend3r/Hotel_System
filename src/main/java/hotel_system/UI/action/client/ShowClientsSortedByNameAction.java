package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowClientsSortedByNameAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientsSortedByNameAction.class);
    private final ManagerHotel manager;

    public ShowClientsSortedByNameAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения клиентов по алфавиту");
        try {
            System.out.println("\nКлиенты (по алфавиту):");
            long count = manager.getAllActiveBookings(SortType.ALPHABET)
                    .stream()
                    .peek(c -> System.out.println(c.getClient().getName() + " " + c.getClient().getSurname()))
                    .count();

            logger.info("Отображено {} клиентов по алфавиту", count);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов по алфавиту: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов по алфавиту: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}