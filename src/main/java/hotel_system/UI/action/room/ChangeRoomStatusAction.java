package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.RoomCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ChangeRoomStatusAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ChangeRoomStatusAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public ChangeRoomStatusAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало смены статуса комнаты");
        try {
            System.out.println("\nСмена статуса:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новый статус (1-READY, 2-ON_REPAIR, 3-CLEANING_REQUIRED): ");
            int status = scanner.nextInt();

            RoomCondition newStatus = RoomCondition.values()[status-1];
            logger.info("Смена статуса комнаты {} на {}", number, newStatus);

            manager.updateRoomStatus(number, newStatus);
            System.out.println("Статус обновлен");
            logger.info("Статус комнаты {} успешно обновлен на {}", number, newStatus);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при смене статуса комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при смене статуса: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        } finally {
            scanner.nextLine();
        }
    }
}