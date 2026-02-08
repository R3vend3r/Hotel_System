package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class showClientAmenitiesSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientAmenitiesSortedByPriceAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showClientAmenitiesSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента по цене");
        try {
            System.out.print("\nУслуги клиента (по цене)\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг по цене для клиента в комнате {}", roomNumber);

            manager.findClientByRoom(roomNumber).ifPresent(client ->
                    manager.getClientAmenitiesSorted(client, SortType.PRICE)
                            .forEach(a -> System.out.printf("%.2f руб. - %s%n",
                                    a.getAmenity().getPrice(), a.getAmenity().getName()))
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