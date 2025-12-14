package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.RoomType;
import model.Room;

import java.util.Scanner;

public class addRoomAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addRoomAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            performRoomAddition();
        } catch (Exception e) {
            handleAdditionError(e);
        }
    }

    private void performRoomAddition() {
        printAdditionHeader();
        Room room = createRoomFromInput();
        addRoomToSystem(room);
        printSuccessMessage();
    }

    private void printAdditionHeader() {
        System.out.println("\nДобавление номера:");
    }

    private Room createRoomFromInput() {
        int number = readRoomNumber();
        RoomType roomType = selectRoomType();
        double price = readRoomPrice();
        int capacity = readRoomCapacity();

        return new Room(number, roomType, price, capacity);
    }

    private int readRoomNumber() {
        System.out.print("Номер комнаты: ");
        return scanner.nextInt();
    }

    private RoomType selectRoomType() {
        displayRoomTypeOptions();
        int typeChoice = readTypeChoice();
        return convertToRoomType(typeChoice);
    }

    private void displayRoomTypeOptions() {
        System.out.println("Тип комнаты (1-5):");
        for (RoomType type : RoomType.values()) {
            System.out.println((type.ordinal() + 1) + ". " + type.name());
        }
    }

    private int readTypeChoice() {
        System.out.print("Выберите тип: ");
        return scanner.nextInt();
    }

    private RoomType convertToRoomType(int choice) {
        return RoomType.values()[choice - 1];
    }

    private double readRoomPrice() {
        System.out.print("Цена за ночь: ");
        return scanner.nextDouble();
    }

    private int readRoomCapacity() {
        System.out.print("Вместимость: ");
        return scanner.nextInt();
    }

    private void addRoomToSystem(Room room) {
        manager.addRoom(room);
    }

    private void printSuccessMessage() {
        System.out.println("Номер добавлен");
    }

    private void handleAdditionError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
        scanner.nextLine();
    }
}