package hotelsystem.UI.action.order;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class evictClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(evictClientAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public evictClientAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("evictClientAction: Начало процесса выселения клиента");
        try {
            performEvictionProcess();
            logger.info("evictClientAction: Процесс выселения завершен успешно");
        } catch (IllegalArgumentException e) {
            logger.error("evictClientAction: Ошибка ввода данных: {}", e.getMessage());
            handleInputError(e);
        } catch (NoSuchElementException e) {
            logger.error("evictClientAction: Клиент не найден: {}", e.getMessage());
            handleNotFoundError(e);
        } catch (Exception e) {
            logger.error("evictClientAction: Системная ошибка при выселении: {}", e.getMessage(), e);
            handleExecutionError(e);
        }
    }

    private void performEvictionProcess() {
        printEvictionHeader();
        int roomNumber = readRoomNumber();
        processClientEviction(roomNumber);
    }

    private void printEvictionHeader() {
        System.out.println("\n=== Выселение клиента ===");
    }

    private int readRoomNumber() {
        System.out.print("Номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        logger.info("evictClientAction: Запрос на выселение из комнаты {}", roomNumber);
        return roomNumber;
    }

    private void processClientEviction(int roomNumber) {
        manager.findClientByRoom(roomNumber).ifPresentOrElse(
                this::confirmAndEvictClient,
                this::handleRoomEmptyOrNotFound
        );
    }

    private void confirmAndEvictClient(Client client) {
        printClientInfo(client);
        if (confirmEviction()) {
            executeClientEviction(client);
        } else {
            logger.info("evictClientAction: Пользователь отменил выселение клиента {}", client.getId());
        }
    }

    private void printClientInfo(Client client) {
        System.out.println("Клиент: " + client);
    }

    private boolean confirmEviction() {
        System.out.print("Выселить (да/нет)? ");
        return scanner.nextLine().equalsIgnoreCase("да");
    }

    private void executeClientEviction(Client client) {
        try {
            logger.info("evictClientAction: Выполнение выселения клиента {} из комнаты {}",
                    client.getId(), client.getRoomNumber());
            manager.evictClient(client.getRoomNumber());
            System.out.println("Клиент выселен");
            logger.info("evictClientAction: Клиент {} успешно выселен из комнаты {}",
                    client.getId(), client.getRoomNumber());
        } catch (SQLException e) {
            logger.error("evictClientAction: Ошибка БД при выселении клиента {}: {}",
                    client.getId(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void handleRoomEmptyOrNotFound() {
        logger.warn("evictClientAction: Комната пуста или не существует");
        System.out.println("Номер свободен или не существует");
    }

    private void handleExecutionError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }

    private void handleInputError(IllegalArgumentException e) {
        System.err.println("Ошибка ввода: " + e.getMessage());
    }

    private void handleNotFoundError(NoSuchElementException e) {
        System.err.println("Ошибка поиска: " + e.getMessage());
    }
}