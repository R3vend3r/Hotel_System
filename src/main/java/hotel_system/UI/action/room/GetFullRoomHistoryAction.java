package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.UI.action.Action;
import hotel_system.controller.OrderController;
import hotel_system.dto.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class GetFullRoomHistoryAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetFullRoomHistoryAction.class);
    private final OrderController orderController;
    private final Scanner scanner = new Scanner(System.in);

    public GetFullRoomHistoryAction(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.debug("Начало получения полной истории комнаты");
        try {
            System.out.print("\nПолная история номера\nВведите номер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Получение истории для комнаты {}", roomNumber);
            List<ClientResponse> history = orderController.getRoomHistory(roomNumber);

            if (history.isEmpty()) {
                System.out.println("История для комнаты " + roomNumber + " пуста");
                logger.info("История комнаты {} пуста", roomNumber);
            } else {
                System.out.println("\n=== Полная история комнаты " + roomNumber + " ===");
                System.out.println("Всего записей: " + history.size());
                System.out.println("----------------------------------------");

                history.forEach(client ->
                        System.out.println("• " + client.name() + " " + client.surname() +
                                " (ID: " + client.id() +
                                (client.roomNumber() != null ? ", комната: " + client.roomNumber() : "") + ")")
                );

                logger.info("Получена история комнаты {} - {} записей",
                        roomNumber, history.size());
            }

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при получении истории комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при получении истории: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}