package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class checkRoomAvailabilityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(checkRoomAvailabilityAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public checkRoomAvailabilityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("checkRoomAvailabilityAction: Начало проверки доступности комнаты");
        try {
            System.out.print("\nПроверка номера\nВведите номер: ");
            int number = scanner.nextInt();
            scanner.nextLine();

            logger.info("checkRoomAvailabilityAction: Проверка доступности комнаты {}", number);
            boolean isAvailable = manager.isRoomAvailable(number);

            System.out.println(isAvailable ? "Свободен" : "Занят");
            logger.info("checkRoomAvailabilityAction: Комната {} - {}", number, isAvailable ? "свободна" : "занята");

        } catch (Exception e) {
            logger.error("checkRoomAvailabilityAction: Ошибка при проверке доступности: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}