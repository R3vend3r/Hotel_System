package hotelsystem.UI.action.amenity;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class showClientAmenitiesSortedByDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientAmenitiesSortedByDateAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showClientAmenitiesSortedByDateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента по дате");
        try {
            System.out.print("\nУслуги клиента (по дате)\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг по дате для клиента в комнате {}", roomNumber);

            manager.findClientByRoom(roomNumber).ifPresent(client ->
                    manager.getClientAmenitiesSorted(client, SortType.DATE_END)
                            .forEach(a -> System.out.printf("%s - %s%n",
                                    a.getServiceDate(), a.getAmenity().getName()))
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