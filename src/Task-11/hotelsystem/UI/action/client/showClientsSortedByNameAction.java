package hotelsystem.UI.action.client;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showClientsSortedByNameAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientsSortedByNameAction.class);
    private final ManagerHotel manager;

    public showClientsSortedByNameAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showClientsSortedByNameAction: Начало отображения клиентов по алфавиту");
        try {
            System.out.println("\nКлиенты (по алфавиту):");
            long count = manager.getAllActiveBookings(SortType.ALPHABET)
                    .stream()
                    .peek(c -> System.out.println(c.getClient().getName() + " " + c.getClient().getSurname()))
                    .count();

            logger.info("showClientsSortedByNameAction: Отображено {} клиентов по алфавиту", count);

        } catch (Exception e) {
            logger.error("showClientsSortedByNameAction: Ошибка при отображении клиентов по алфавиту: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}