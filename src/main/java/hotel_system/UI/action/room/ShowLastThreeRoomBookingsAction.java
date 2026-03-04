package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.RoomBookingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ShowLastThreeRoomBookingsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowLastThreeRoomBookingsAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);

    public ShowLastThreeRoomBookingsAction(OrderController orderController, ClientController clientController) {
        this.orderController = orderController;
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения последних постояльцев");
        try {
            System.out.print("\nПоследние постояльцы\nВведите номер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Получение последних постояльцев для комнаты {}", roomNumber);
            List<RoomBookingResponse> bookings = orderController.getLastThreeBookingsForRoom(roomNumber);
            int limit = Math.min(bookings.size(), 3);

            if (limit == 0) {
                System.out.println("История для комнаты " + roomNumber + " пуста");
                logger.info("История для комнаты {} пуста", roomNumber);
            } else {
                System.out.println("\n=== Последние " + limit + " постояльца комнаты " + roomNumber + " ===");

                for (RoomBookingResponse booking : bookings.subList(0, limit)) {
                    String clientId = booking.clientId();
                    clientController.findClientById(clientId)
                            .ifPresentOrElse(
                                    client -> System.out.println("• " + client.name() + " " + client.surname() +
                                            " (ID: " + client.id() + ")"),
                                    () -> System.out.println("• Клиент #" + clientId + " (информация недоступна)")
                            );
                }

                logger.info("Отображено {} последних постояльцев комнаты {}", limit, roomNumber);
            }
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении последних постояльцев: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении последних постояльцев: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}