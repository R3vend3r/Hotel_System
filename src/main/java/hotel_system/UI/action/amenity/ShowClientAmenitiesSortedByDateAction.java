package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.AmenityController;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ShowClientAmenitiesSortedByDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientAmenitiesSortedByDateAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final AmenityController amenityController;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public ShowClientAmenitiesSortedByDateAction(OrderController orderController,
                                                 ClientController clientController,
                                                 AmenityController amenityController) {
        this.orderController = orderController;
        this.clientController = clientController;
        this.amenityController = amenityController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента по дате");
        try {
            System.out.print("\nУслуги клиента (по дате)\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг по дате для клиента в комнате {}", roomNumber);

            clientController.findClientByRoom(roomNumber)
                    .ifPresentOrElse(
                            client -> {
                                System.out.println("\n=== Услуги клиента " + client.name() + " " + client.surname() + " (по дате) ===");

                                var orders = orderController.getClientAmenitiesSorted(client.id(), SortType.DATE_END);

                                if (orders.isEmpty()) {
                                    System.out.println("У клиента нет заказанных услуг");
                                } else {
                                    orders.forEach(order -> {
                                        String dateStr = order.serviceDate() != null
                                                ? dateFormat.format(order.serviceDate())
                                                : "дата не указана";

                                        String amenityName = amenityController.findAmenityById(order.amenityId())
                                                .map(AmenityResponse::name)
                                                .orElse("Неизвестная услуга");

                                        System.out.printf("• %s - %s (%.2f руб.)%n",
                                                dateStr,
                                                amenityName,
                                                order.totalPrice());
                                    });
                                }
                            },
                            () -> System.out.println("Клиент в комнате " + roomNumber + " не найден")
                    );

            logger.info("Услуги клиента по дате в комнате {} успешно отображены", roomNumber);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг клиента по дате: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг клиента по дате: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}