package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.AmenityController;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.AmenityOrderResponse;
import hotel_system.dto.AmenityResponse;
import hotel_system.dto.ClientResponse;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ShowClientAmenitiesAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientAmenitiesAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final AmenityController amenityController;
    private final Scanner scanner = new Scanner(System.in);

    public ShowClientAmenitiesAction(OrderController orderController,
                                     ClientController clientController,
                                     AmenityController amenityController) {
        this.orderController = orderController;
        this.clientController = clientController;
        this.amenityController = amenityController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента");
        try {
            System.out.print("\nВсе услуги клиента\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг для клиента в комнате {}", roomNumber);

            clientController.findClientByRoom(roomNumber)
                    .ifPresentOrElse(
                            client -> {
                                System.out.println("\n=== Услуги клиента " + client.name() + " " + client.surname() + " ===");

                                var orders = orderController.getClientAmenitiesSorted(client.id(), SortType.NONE);

                                if (orders.isEmpty()) {
                                    System.out.println("У клиента нет заказанных услуг");
                                } else {
                                    orders.forEach(order -> {
                                        String amenityName = amenityController.findAmenityById(order.amenityId())
                                                .map(AmenityResponse::name)
                                                .orElse("Неизвестная услуга");

                                        System.out.printf("• %s - %.2f руб.%n",
                                                amenityName,
                                                order.totalPrice());
                                    });
                                }
                            },
                            () -> System.out.println("Клиент в комнате " + roomNumber + " не найден")
                    );

            logger.info("Услуги клиента в комнате {} успешно отображены", roomNumber);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг клиента: {}", e.getMessage(), e);
            System.out.println("Ошибка при отображении услуг клиента: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг клиента: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}