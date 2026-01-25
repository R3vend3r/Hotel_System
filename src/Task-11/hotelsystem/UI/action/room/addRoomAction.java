package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.RoomType;
import hotelsystem.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class addRoomAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(addRoomAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addRoomAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("addRoomAction: Начало добавления номера");
        try {
            performRoomAddition();
            logger.info("addRoomAction: Добавление номера завершено успешно");
        } catch (Exception e) {
            logger.error("addRoomAction: Ошибка при добавлении номера: {}", e.getMessage(), e);
            handleAdditionError(e);
        }
    }

    private void performRoomAddition() throws SQLException {
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

        logger.info("addRoomAction: Создание комнаты {} типа {} ценой {} вместимостью {}",
                number, roomType, price, capacity);
        return new Room(number, roomType, price, capacity);
    }

    private int readRoomNumber() {
        System.out.print("Номер комнаты: ");
        int number = scanner.nextInt();
        logger.debug("addRoomAction: Введен номер комнаты: {}", number);
        return number;
    }

    private RoomType selectRoomType() {
        displayRoomTypeOptions();
        int typeChoice = readTypeChoice();
        RoomType type = convertToRoomType(typeChoice);
        logger.debug("addRoomAction: Выбран тип комнаты: {}", type);
        return type;
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
        double price = scanner.nextDouble();
        logger.debug("addRoomAction: Введена цена: {}", price);
        return price;
    }

    private int readRoomCapacity() {
        System.out.print("Вместимость: ");
        int capacity = scanner.nextInt();
        logger.debug("addRoomAction: Введена вместимость: {}", capacity);
        return capacity;
    }

    private void addRoomToSystem(Room room) throws SQLException {
        logger.info("addRoomAction: Добавление комнаты {} в систему", room.getNumberRoom());
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