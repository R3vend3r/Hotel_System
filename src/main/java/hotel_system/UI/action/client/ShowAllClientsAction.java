package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAllClientsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllClientsAction.class);
    private final ManagerHotel manager;

    public ShowAllClientsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения всех клиентов");
        try {
            System.out.println("\n=== Список клиентов ===");
            int clientCount = 0;

            manager.getAllClients().forEach(client -> {
                Integer roomNumber = client.getRoomNumber();
                String roomInfo = (roomNumber != null && roomNumber > 0) ?
                        "Номер " + roomNumber : "Не заселен";
                System.out.printf("%s %s | %s | ID: %s%n",
                        client.getName(),
                        client.getSurname(),
                        roomInfo,
                        client.getId());
            });

            clientCount = manager.getAllClients().size();
            logger.info("Отображено {} клиентов", clientCount);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}