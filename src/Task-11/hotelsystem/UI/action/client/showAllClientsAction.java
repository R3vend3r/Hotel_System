package hotelsystem.UI.action.client;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAllClientsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAllClientsAction.class);
    private final ManagerHotel manager;

    public showAllClientsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showAllClientsAction: Начало отображения всех клиентов");
        try {
            System.out.println("\n=== Список клиентов ===");
            int clientCount = 0;

            manager.getAllClients().forEach(client -> {
                String roomInfo = client.getRoomNumber() > 0 ?
                        "Номер " + client.getRoomNumber() : "Не заселен";
                System.out.printf("%s %s | %s | ID: %s%n",
                        client.getName(),
                        client.getSurname(),
                        roomInfo,
                        client.getId());
            });

            clientCount = manager.getAllClients().size();
            logger.info("showAllClientsAction: Отображено {} клиентов", clientCount);

        } catch (Exception e) {
            logger.error("showAllClientsAction: Ошибка при отображении клиентов: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}