package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.RoomBookingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShowAllCompletedBookingsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllCompletedBookingsAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ShowAllCompletedBookingsAction(OrderController orderController, ClientController clientController) {
        this.orderController = orderController;
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения завершенных бронирований");
        try {
            System.out.println("\n=== ЗАВЕРШЕННЫЕ БРОНИРОВАНИЯ ===");

            List<RoomBookingResponse> bookings = orderController.getAllCompletedBookings();

            if (bookings.isEmpty()) {
                System.out.println("Нет завершенных бронирований");
                logger.info("Нет завершенных бронирований");
                return;
            }

            System.out.println("Найдено " + bookings.size() + " завершенных бронирований:");
            System.out.println("=".repeat(60));
            System.out.printf("%-4s | %-20s | %-10s | %-10s%n",
                    "№", "Клиент", "Комната", "Дата выезда");
            System.out.println("-".repeat(60));

            int count = 0;
            for (RoomBookingResponse booking : bookings) {
                count++;

                // Получаем информацию о клиенте
                String clientInfo = clientController.findClientById(booking.clientId())
                        .map(client -> client.name() + " " + client.surname())
                        .orElse("Клиент #" + booking.clientId());

                String checkOutDateStr = booking.checkOutDate() != null
                        ? dateFormat.format(booking.checkOutDate())
                        : "не указана";

                System.out.printf("%-4d | %-20s | %-10d | %-10s%n",
                        count,
                        truncateString(clientInfo),
                        booking.roomNumber(),
                        checkOutDateStr);
            }

            System.out.println("=".repeat(60));
            logger.info("Отображено {} завершенных бронирований", bookings.size());

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении завершенных бронирований: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении завершенных бронирований: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    private String truncateString(String str) {
        if (str == null) return "";
        if (str.length() <= 20) return str;
        return str.substring(0, 20 - 3) + "...";
    }
}