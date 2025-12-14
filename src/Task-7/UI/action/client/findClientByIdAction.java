package UI.action.client;

import Controller.ManagerHotel;
import UI.action.Action;
import model.Client;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class findClientByIdAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public findClientByIdAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            performClientSearch();
        } catch (IllegalArgumentException e) {
            handleInputError(e);
        } catch (NoSuchElementException e) {
            handleNotFoundError(e);
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private void performClientSearch() {
        printSearchHeader();
        String clientId = readClientId();
        validateClientId(clientId);
        findAndDisplayClient(clientId);
    }

    private void printSearchHeader() {
        System.out.println("\n=== Поиск клиента ===");
    }

    private String readClientId() {
        System.out.print("Введите ID клиента: ");
        return scanner.nextLine().trim();
    }

    private void validateClientId(String clientId) {
        if (clientId.isEmpty()) {
            throw new IllegalArgumentException("ID клиента не может быть пустым");
        }
    }

    private void findAndDisplayClient(String clientId) {
        manager.findClientById(clientId)
                .ifPresentOrElse(
                        this::displayFoundClient,
                        () -> handleClientNotFound(clientId)
                );
    }

    private void displayFoundClient(Client client) {
        System.out.println("\nНайден клиент:\n" + client);
    }

    private void handleClientNotFound(String clientId) {
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