package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class showLastThreeRoomBookingsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showLastThreeRoomBookingsAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showLastThreeRoomBookingsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showLastThreeRoomBookingsAction: Начало отображения последних постояльцев");
        try {
            System.out.print("\nПоследние постояльцы\nВведите номер: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("showLastThreeRoomBookingsAction: Получение последних постояльцев для комнаты {}", roomNumber);
            List<Client> history = manager.getRoomHistory(roomNumber);
            int limit = Math.min(history.size(), 3);

            if (limit == 0) {
                System.out.println("История для комнаты " + roomNumber + " пуста");
                logger.info("showLastThreeRoomBookingsAction: История для комнаты {} пуста", roomNumber);
            } else {
                System.out.println("Последние " + limit + " постояльца комнаты " + roomNumber + ":");
                history.subList(0, limit).forEach(client ->
                        System.out.println("- " + client.getName() + " " + client.getSurname())
                );
                logger.info("showLastThreeRoomBookingsAction: Отображено {} последних постояльцев комнаты {}", limit, roomNumber);
            }
        } catch (Exception e) {
            logger.error("showLastThreeRoomBookingsAction: Ошибка при отображении последних постояльцев: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}