package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class EvictClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(EvictClientAction.class);
    private final ClientController clientController;
    private final OrderController orderController;
    private final Scanner scanner = new Scanner(System.in);

    public EvictClientAction(ClientController clientController, OrderController orderController) {
        this.clientController = clientController;
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.debug("Начало процесса выселения клиента");
        try {
            performEvictionProcess();
            logger.info("Процесс выселения завершен успешно");
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при выселении клиента: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка ввода данных: {}", e.getMessage());
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error("Клиент не найден: {}", e.getMessage());
            System.out.println("Ошибка поиска: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Системная ошибка при выселении: {}", e.getMessage(), e);
            System.out.println("Системная ошибка: " + e.getMessage());
        }
    }

    private void performEvictionProcess() {
        System.out.println("\n=== Выселение клиента ===");
        int roomNumber = readRoomNumber();
        processClientEviction(roomNumber);
    }

    private int readRoomNumber() {
        System.out.print("Номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        logger.info("Запрос на выселение из комнаты {}", roomNumber);
        return roomNumber;
    }

    private void processClientEviction(int roomNumber) {
        clientController.findClientByRoom(roomNumber).ifPresentOrElse(
                this::confirmAndEvictClient,
                this::handleRoomEmptyOrNotFound
        );
    }

    private void confirmAndEvictClient(ClientResponse client) {
        System.out.println("Клиент: " + client);
        System.out.print("Выселить (да/нет)? ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("да")) {
            try {
                executeClientEviction(client);
            } catch (Exception e) {
                logger.error("Ошибка при выполнении выселения", e);
                System.out.println("Ошибка при выселении: " + e.getMessage());
            }
        } else {
            logger.info("Пользователь отменил выселение клиента {}", client.id());
            System.out.println("Выселение отменено");
        }
    }

    private void executeClientEviction(ClientResponse client) {
        logger.info("Выполнение выселения клиента {} из комнаты {}",
                client.id(), client.roomNumber());
        orderController.evictClient(client.roomNumber());
        System.out.println("Клиент успешно выселен");
        logger.info("Клиент {} успешно выселен из комнаты {}",
                client.id(), client.roomNumber());
    }

    private void handleRoomEmptyOrNotFound() {
        logger.warn("Комната пуста или не существует");
        System.out.println("Номер свободен или не существует");
    }
}