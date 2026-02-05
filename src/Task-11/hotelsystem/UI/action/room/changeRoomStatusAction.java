package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.RoomCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class changeRoomStatusAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(changeRoomStatusAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public changeRoomStatusAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("changeRoomStatusAction: Начало смены статуса комнаты");
        try {
            System.out.println("\nСмена статуса:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новый статус (1-READY, 2-ON_REPAIR, 3-CLEANING_REQUIRED): ");
            int status = scanner.nextInt();

            RoomCondition newStatus = RoomCondition.values()[status-1];
            logger.info("changeRoomStatusAction: Смена статуса комнаты {} на {}", number, newStatus);

            manager.updateRoomStatus(number, newStatus);
            System.out.println("Статус обновлен");
            logger.info("changeRoomStatusAction: Статус комнаты {} успешно обновлен на {}", number, newStatus);

        } catch (Exception e) {
            logger.error("changeRoomStatusAction: Ошибка при смене статуса комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
            scanner.nextLine();
        }
    }
}