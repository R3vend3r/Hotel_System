package hotelsystem.UI.action.client;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showClientsSortedByCheckoutDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientsSortedByCheckoutDateAction.class);
    private final ManagerHotel manager;

    public showClientsSortedByCheckoutDateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения клиентов по дате выезда");
        try {
            System.out.println("\nКлиенты (по дате выезда):");
            long count = manager.getAllActiveBookings(SortType.DATE_END)
                    .stream()
                    .peek(c -> System.out.printf("%s %s (выезд: %s)%n",
                            c.getClient().getName(), c.getClient().getSurname(), c.getCheckOutDate()))
                    .count();

            logger.info("Отображено {} клиентов по дате выезда", count);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}