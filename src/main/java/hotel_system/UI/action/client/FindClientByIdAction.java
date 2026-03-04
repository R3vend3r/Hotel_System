package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.ClientController;
import hotel_system.UI.action.Action;
import hotel_system.dto.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class FindClientByIdAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(FindClientByIdAction.class);
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);

    public FindClientByIdAction(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало поиска клиента по ID");
        try {
            performClientSearch();
            logger.info("Поиск клиента завершен успешно");
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при поиске клиента по ID: {}", e.getMessage(), e);
            System.err.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (NoSuchElementException e) {
            logger.error("Клиент не найден: {}", e.getMessage());
            System.err.println("Ошибка поиска: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Системная ошибка при поиске клиента: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
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
            logger.warn("Введен пустой ID клиента");
            throw new IllegalArgumentException("ID клиента не может быть пустым");
        }
    }

    private void findAndDisplayClient(String clientId) {
        logger.info("Поиск клиента с ID: {}", clientId);
        clientController.findClientById(clientId)
                .ifPresentOrElse(
                        this::displayFoundClient,
                        () -> handleClientNotFound(clientId)
                );
    }

    private void displayFoundClient(ClientResponse client) {
        logger.info("Клиент найден: {} {} (ID: {})",
                client.name(), client.surname(), client.id());
        System.out.println("\nНайден клиент:\n" + client);
    }

    private void handleClientNotFound(String clientId) {
        logger.warn("Клиент с ID '{}' не найден", clientId);
        throw new NoSuchElementException("Клиент с ID '" + clientId + "' не найден");
    }
}