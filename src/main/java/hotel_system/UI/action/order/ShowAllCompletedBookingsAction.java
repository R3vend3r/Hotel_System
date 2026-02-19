package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAllCompletedBookingsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllCompletedBookingsAction.class);
    private final ManagerHotel manager;

    public ShowAllCompletedBookingsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения завершенных бронирований");
        try {
            System.out.println("\n=== Завершенные бронирования ===");

            var bookings = manager.getAllCompletedBookings();

            if (bookings.isEmpty()) {
                System.out.println("Нет завершенных бронирований");
            } else {
                System.out.println("Найдено " + bookings.size() + " завершенных бронирований:");
                System.out.println("-".repeat(40));

                bookings.forEach(booking ->
                        System.out.printf("Номер %d - %s (выезд: %s)%n",
                                booking.getRoomNumber(),
                                booking.getClientInfo(),
                                booking.getCheckOutDate())
                );

                System.out.println("-".repeat(40));
            }

            logger.info("Отображено {} завершенных бронирований", bookings.size());

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении завершенных бронирований: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении завершенных бронирований: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}