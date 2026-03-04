package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShowClientsSortedByCheckoutDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientsSortedByCheckoutDateAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ShowClientsSortedByCheckoutDateAction(OrderController orderController, ClientController clientController) {
        this.orderController = orderController;
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения клиентов по дате выезда");
        try {
            System.out.println("\n=== КЛИЕНТЫ (ПО ДАТЕ ВЫЕЗДА) ===");

            List<RoomBookingResponse> activeBookings = orderController.getAllActiveBookings(SortType.DATE_END);

            if (activeBookings.isEmpty()) {
                System.out.println("Нет активных клиентов");
                logger.info("Нет активных клиентов для отображения");
                return;
            }

            System.out.println("Всего записей: " + activeBookings.size());
            System.out.println("-".repeat(50));

            activeBookings.forEach(booking -> {
                // Получаем информацию о клиенте
                String clientName = clientController.findClientById(booking.clientId())
                        .map(client -> client.name() + " " + client.surname())
                        .orElse("Неизвестный клиент (ID: " + booking.clientId() + ")");

                String checkOutDateStr = booking.checkOutDate() != null
                        ? dateFormat.format(booking.checkOutDate())
                        : "не указана";

                System.out.printf("• %s | Комната %d | Выезд: %s%n",
                        clientName,
                        booking.roomNumber(),
                        checkOutDateStr);
            });

            System.out.println("-".repeat(50));
            logger.info("Отображено {} клиентов по дате выезда", activeBookings.size());

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}