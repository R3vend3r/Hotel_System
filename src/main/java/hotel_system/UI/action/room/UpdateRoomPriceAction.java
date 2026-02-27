package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class UpdateRoomPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(UpdateRoomPriceAction.class);
    private final RoomController controller;
    private final Scanner scanner = new Scanner(System.in);

    public UpdateRoomPriceAction(RoomController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        logger.debug("Начало изменения цены комнаты");
        try {
            System.out.println("\nИзменение цены:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новая цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            logger.info("Изменение цены комнаты {} на {}", number, price);
            controller.updateRoomPrice(number, price);

            System.out.println("Цена обновлена");
            logger.info("Цена комнаты {} успешно обновлена на {}", number, price);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при изменении цены комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при изменении цены комнаты: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        } finally {
            scanner.nextLine();
        }
    }
}