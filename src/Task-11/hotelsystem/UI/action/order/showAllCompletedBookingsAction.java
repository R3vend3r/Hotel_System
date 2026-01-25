package hotelsystem.UI.action.order;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAllCompletedBookingsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAllCompletedBookingsAction.class);
    private final ManagerHotel manager;

    public showAllCompletedBookingsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showAllCompletedBookingsAction: Начало отображения завершенных бронирований");
        try {
            System.out.println("\n=== Завершенные бронирования ===");
            long count = manager.getAllCompletedBookings()
                    .stream()
                    .peek(booking -> System.out.printf("Номер %d - %s (выезд: %s)%n",
                            booking.getRoom().getNumberRoom(),
                            booking.getClient(),
                            booking.getCheckOutDate()))
                    .count();

            logger.info("showAllCompletedBookingsAction: Отображено {} завершенных бронирований", count);

        } catch (Exception e) {
            logger.error("showAllCompletedBookingsAction: Ошибка при отображении завершенных бронирований: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}