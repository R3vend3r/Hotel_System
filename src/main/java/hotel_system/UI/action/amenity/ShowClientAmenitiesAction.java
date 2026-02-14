package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ShowClientAmenitiesAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientAmenitiesAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public ShowClientAmenitiesAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг клиента");
        try {
            System.out.print("\nВсе услуги клиента\nНомер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Поиск услуг для клиента в комнате {}", roomNumber);

            manager.findClientByRoom(roomNumber).ifPresent(client ->
                    manager.getClientAmenitiesSorted(client, SortType.NONE)
                            .forEach(a -> System.out.println(a.getAmenity()))
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