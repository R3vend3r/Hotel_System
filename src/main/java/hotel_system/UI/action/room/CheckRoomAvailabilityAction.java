package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CheckRoomAvailabilityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(CheckRoomAvailabilityAction.class);
    private final RoomController roomController;
    private final Scanner scanner = new Scanner(System.in);

    public CheckRoomAvailabilityAction(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void execute() {
        logger.debug("Начало проверки доступности комнаты");
        try {
            System.out.print("\nПроверка номера\nВведите номер: ");
            int number = scanner.nextInt();
            scanner.nextLine();

            logger.info("Проверка доступности комнаты {}", number);
            boolean isAvailable = roomController.isRoomAvailable(number);

            System.out.println(isAvailable ? "Свободен" : "Занят");
            logger.info("Комната {} - {}", number, isAvailable ? "свободна" : "занята");

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при проверке доступности: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при проверке: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}