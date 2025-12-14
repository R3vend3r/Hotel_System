package UI.action.order;

import Controller.ManagerHotel;
import UI.action.Action;
import model.Client;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class evictClientAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public evictClientAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            performEvictionProcess();
        } catch (IllegalArgumentException e) {
            handleInputError(e);
        } catch (NoSuchElementException e) {
            handleNotFoundError(e);
        } catch (Exception e) {
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
        manager.evictClient(client.getRoomNumber());
        System.out.println("Клиент выселен");
    }

    private void handleRoomEmptyOrNotFound() {
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