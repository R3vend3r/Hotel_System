package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.InputMismatchException;
import java.util.Scanner;

public class showRoomDetailsAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showRoomDetailsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            displayRoomDetails();
        } catch (InputMismatchException e) {
            handleInputMismatchError(e);
        } catch (IllegalArgumentException e) {
            handleIllegalArgumentError(e);
        } catch (Exception e) {
            handleUnexpectedError(e);
        }
    }

    private void displayRoomDetails() {
        int roomNumber = readRoomNumber();
        String roomDetails = getRoomDetailsFromManager(roomNumber);
        printRoomDetails(roomDetails);
    }

    private int readRoomNumber() {
        System.out.print("\nВведите номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        return roomNumber;
    }

    private String getRoomDetailsFromManager(int roomNumber) {
        return manager.getRoomDetails(roomNumber);
    }

    private void printRoomDetails(String roomDetails) {
        System.out.println(roomDetails);
    }

    private void handleInputMismatchError(InputMismatchException e) {
        System.err.println("Ошибка: Номер комнаты должен быть целым числом");
        scanner.nextLine();
    }

    private void handleIllegalArgumentError(IllegalArgumentException e) {
        System.err.println("Ошибка: " + e.getMessage());
    }

    private void handleUnexpectedError(Exception e) {
        System.err.println("Неожиданная ошибка: " + e.getMessage());
    }
}