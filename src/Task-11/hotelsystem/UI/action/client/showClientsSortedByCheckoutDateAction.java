package hotelsystem.UI.action.client;

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
        logger.info("showClientsSortedByCheckoutDateAction: Начало отображения клиентов по дате выезда");
        try {
            System.out.println("\nКлиенты (по дате выезда):");
            long count = manager.getAllActiveBookings(SortType.DATE_END)
                    .stream()
                    .peek(c -> System.out.printf("%s %s (выезд: %s)%n",
                            c.getClient().getName(), c.getClient().getSurname(), c.getCheckOutDate()))
                    .count();

            logger.info("showClientsSortedByCheckoutDateAction: Отображено {} клиентов по дате выезда", count);

        } catch (Exception e) {
            logger.error("showClientsSortedByCheckoutDateAction: Ошибка при отображении клиентов по дате выезда: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}