package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.entity.Client;
import hotelsystem.model.entity.RoomBooking;
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
        logger.debug("Начало отображения последних постояльцев");
        try {
            System.out.print("\nПоследние постояльцы\nВведите номер: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Получение последних постояльцев для комнаты {}", roomNumber);
            List<RoomBooking> bookings = manager.getLastThreeBookingsForRoom(roomNumber);
            int limit = Math.min(bookings.size(), 3);

            if (limit == 0) {
                System.out.println("История для комнаты " + roomNumber + " пуста");
                logger.info("История для комнаты {} пуста", roomNumber);
            } else {
                System.out.println("Последние " + limit + " постояльца комнаты " + roomNumber + ":");

                for (RoomBooking booking : bookings.subList(0, limit)) {
                    String clientId = booking.getClientId();
                    var clientOpt = manager.findClientById(clientId);

                    if (clientOpt.isPresent()) {
                        Client client = clientOpt.get();
                        System.out.println("- " + client.getName() + " " + client.getSurname());
                    } else {
                        System.out.println("- Клиент #" + clientId + " (информация недоступна)");
                    }
                }

                logger.info("Отображено {} последних постояльцев комнаты {}", limit, roomNumber);
            }
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении последних постояльцев: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении последних постояльцев: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}