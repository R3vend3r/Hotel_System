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

import java.util.Scanner;

public class ShowClientAmenitiesSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientAmenitiesSortedByPriceAction.class);
    private final OrderController orderController;
    private final ClientController clientController;
    private final AmenityController amenityController;
    private final Scanner scanner = new Scanner(System.in);

    public ShowClientAmenitiesSortedByPriceAction(OrderController orderController,
                                                  ClientController clientController,
                                                  AmenityController amenityController) {
        this.orderController = orderController;
        this.clientController = clientController;
        this.amenityController = amenityController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента по цене");
        try {
            System.out.print("\nУслуги клиента (по цене)\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг по цене для клиента в комнате {}", roomNumber);

            clientController.findClientByRoom(roomNumber)
                    .ifPresentOrElse(
                            client -> {
                                System.out.println("\n=== Услуги клиента " + client.name() + " " + client.surname() + " (по цене) ===");

                                var orders = orderController.getClientAmenitiesSorted(client.id(), SortType.PRICE);

                                if (orders.isEmpty()) {
                                    System.out.println("У клиента нет заказанных услуг");
                                } else {
                                    orders.forEach(order -> {
                                        String amenityName = amenityController.findAmenityById(order.amenityId())
                                                .map(AmenityResponse::name)
                                                .orElse("Неизвестная услуга");

                                        System.out.printf("• %.2f руб. - %s%n",
                                                order.totalPrice(),
                                                amenityName);
                                    });
                                }
                            },
                            () -> System.out.println("Клиент в комнате " + roomNumber + " не найден")
                    );

            logger.info("Услуги клиента по цене в комнате {} успешно отображены", roomNumber);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг клиента по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг клиента по цене: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}