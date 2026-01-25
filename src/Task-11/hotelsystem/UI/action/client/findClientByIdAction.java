package hotelsystem.UI.action.client;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class findClientByIdAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(findClientByIdAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public findClientByIdAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("findClientByIdAction: Начало поиска клиента по ID");
        try {
            performClientSearch();
            logger.info("findClientByIdAction: Поиск клиента завершен успешно");
        } catch (IllegalArgumentException e) {
            logger.error("findClientByIdAction: Ошибка ввода данных: {}", e.getMessage());
            handleInputError(e);
        } catch (NoSuchElementException e) {
            logger.error("findClientByIdAction: Клиент не найден: {}", e.getMessage());
            handleNotFoundError(e);
        } catch (Exception e) {
            logger.error("findClientByIdAction: Системная ошибка при поиске клиента: {}", e.getMessage(), e);
            handleSystemError(e);
        }
    }

    private void performClientSearch() {
        String clientId = readClientId();
        validateClientId(clientId);
        findAndDisplayClient(clientId);
    }

    private String readClientId() {
        System.out.print("Введите ID клиента: ");
        return scanner.nextLine().trim();
    }

    private void validateClientId(String clientId) {
        if (clientId.isEmpty()) {
            logger.warn("findClientByIdAction: Введен пустой ID клиента");
            throw new IllegalArgumentException("ID клиента не может быть пустым");
        }
    }

    private void findAndDisplayClient(String clientId) {
        logger.info("findClientByIdAction: Поиск клиента с ID: {}", clientId);
        manager.findClientById(clientId)
                .ifPresentOrElse(
                        this::displayFoundClient,
                        () -> handleClientNotFound(clientId)
                );
    }

    private void displayFoundClient(Client client) {
        logger.info("findClientByIdAction: Клиент найден: {} {} (ID: {})",
                client.getName(), client.getSurname(), client.getId());
        System.out.println("\nНайден клиент:\n" + client);
    }

    private void handleClientNotFound(String clientId) {
        logger.warn("findClientByIdAction: Клиент с ID '{}' не найден", clientId);
        throw new NoSuchElementException("Клиент с ID '" + clientId + "' не найден");
    }

    private void handleInputError(IllegalArgumentException e) {
        System.err.println("Ошибка ввода: " + e.getMessage());
    }

    private void handleNotFoundError(NoSuchElementException e) {
        System.err.println("Ошибка поиска: " + e.getMessage());
    }

    private void handleSystemError(Exception e) {
        System.err.println("Системная ошибка: " + e.getMessage());
    }
}