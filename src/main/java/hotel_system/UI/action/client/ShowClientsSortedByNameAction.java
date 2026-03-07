package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowClientsSortedByNameAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientsSortedByNameAction.class);
    private final OrderController orderController;
    private final ClientController clientController;

    public ShowClientsSortedByNameAction(OrderController orderController, ClientController clientController) {
        this.orderController = orderController;
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения клиентов по алфавиту");
        try {
            System.out.println("\n=== КЛИЕНТЫ (ПО АЛФАВИТУ) ===");

            List<RoomBookingResponse> activeBookings = orderController.getAllActiveBookings(SortType.ALPHABET);

            if (activeBookings.isEmpty()) {
                System.out.println("Нет активных клиентов");
                logger.info("Нет активных клиентов для отображения");
                return;
            }

            System.out.println("Всего активных клиентов: " + activeBookings.size());
            System.out.println("----------------------------------------");

            AtomicInteger count = new AtomicInteger();
            for (RoomBookingResponse booking : activeBookings) {
                String clientId = booking.clientId();

                clientController.findClientById(clientId)
                        .ifPresentOrElse(
                                client -> {
                                    System.out.printf("%d. %s %s (ID: %s, комната: %d)%n",
                                            count.incrementAndGet(),
                                            client.name(),
                                            client.surname(),
                                            client.id(),
                                            booking.roomNumber());
                                },
                                () -> {
                                    System.out.printf("%d. Клиент #%s (информация недоступна, комната: %d)%n",
                                            count.incrementAndGet(),
                                            clientId,
                                            booking.roomNumber());
                                }
                        );
            }

            logger.info("Отображено {} активных клиентов по алфавиту", count);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов по алфавиту: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов по алфавиту: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}