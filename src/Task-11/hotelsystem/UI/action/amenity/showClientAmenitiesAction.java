package hotelsystem.UI.action.amenity;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class showClientAmenitiesAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showClientAmenitiesAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showClientAmenitiesAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showClientAmenitiesAction: Начало отображения услуг клиента");
        try {
            System.out.print("\nВсе услуги клиента\nНомер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("showClientAmenitiesAction: Поиск услуг для клиента в комнате {}", roomNumber);

            manager.findClientByRoom(roomNumber).ifPresent(client ->
                    manager.getClientAmenitiesSorted(client, SortType.NONE)
                            .forEach(a -> System.out.println(a.getAmenity()))
            );

            logger.info("showClientAmenitiesAction: Услуги клиента в комнате {} успешно отображены", roomNumber);

        } catch (Exception e) {
            logger.error("showClientAmenitiesAction: Ошибка при отображении услуг клиента: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}