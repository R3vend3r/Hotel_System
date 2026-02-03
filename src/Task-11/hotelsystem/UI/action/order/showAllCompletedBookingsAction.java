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

            var bookings = manager.getAllCompletedBookings();

            if (bookings.isEmpty()) {
                System.out.println("Нет завершенных бронирований");
            } else {
                System.out.println("Найдено " + bookings.size() + " завершенных бронирований:");
                System.out.println("----------------------------------------");

                bookings.forEach(booking -> {
                    System.out.printf("Номер %d - %s (выезд: %s)%n",
                            booking.getRoomNumber(),
                            booking.getClientInfo(),
                            booking.getCheckOutDate());
                });

                System.out.println("----------------------------------------");
            }

            logger.info("showAllCompletedBookingsAction: Отображено {} завершенных бронирований", bookings.size());

        } catch (Exception e) {
            logger.error("showAllCompletedBookingsAction: Ошибка при отображении завершенных бронирований: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}