package hotelsystem.UI.action.room;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class getFullRoomHistoryAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(getFullRoomHistoryAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public getFullRoomHistoryAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("getFullRoomHistoryAction: Начало получения полной истории комнаты");
        try {
            System.out.print("\nПолная история номера\nВведите номер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("getFullRoomHistoryAction: Получение истории для комнаты {}", roomNumber);
            List<Client> history = manager.getRoomHistory(roomNumber);

            if (history.isEmpty()) {
                System.out.println("История для комнаты " + roomNumber + " пуста");
                logger.info("getFullRoomHistoryAction: История комнаты {} пуста", roomNumber);
            } else {
                System.out.println("Полная история комнаты " + roomNumber + ":");
                history.forEach(client ->
                        System.out.println("- " + client.getName() +" " + client.getSurname() + " (ID: " + client.getId() + ")")
                );
                logger.info("getFullRoomHistoryAction: Получена история комнаты {} - {} записей",
                        roomNumber, history.size());
            }

        } catch (Exception e) {
            logger.error("getFullRoomHistoryAction: Ошибка при получении истории комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}