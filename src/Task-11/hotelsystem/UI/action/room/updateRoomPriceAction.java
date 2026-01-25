package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class updateRoomPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(updateRoomPriceAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public updateRoomPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("updateRoomPriceAction: Начало изменения цены комнаты");
        try {
            System.out.println("\nИзменение цены:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новая цена: ");
            double price = scanner.nextDouble();

            logger.info("updateRoomPriceAction: Изменение цены комнаты {} на {}", number, price);
            manager.updateRoomPrice(number, price);

            System.out.println("Цена обновлена");
            logger.info("updateRoomPriceAction: Цена комнаты {} успешно обновлена на {}", number, price);

        } catch (Exception e) {
            logger.error("updateRoomPriceAction: Ошибка при изменении цены комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
            scanner.nextLine();
        }
    }
}