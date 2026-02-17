package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.RoomType;
import hotelsystem.model.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.debug("Начало добавления номера");
        try {
            System.out.println("\nДобавление номера:");

            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();

            System.out.println("Тип комнаты (1-5):");
            for (RoomType type : RoomType.values()) {
                System.out.println((type.ordinal() + 1) + ". " + type.name());
            }
            System.out.print("Выберите тип: ");
            int typeChoice = scanner.nextInt();
            RoomType roomType = RoomType.values()[typeChoice - 1];

            System.out.print("Цена за ночь: ");
            double price = scanner.nextDouble();

            System.out.print("Вместимость: ");
            int capacity = scanner.nextInt();
            scanner.nextLine();

            logger.info("Создание комнаты {} типа {} ценой {} вместимостью {}",
                    number, roomType, price, capacity);

            Room room = new Room(number, roomType, price, capacity);
            manager.addRoom(room);

            System.out.println("Номер добавлен");
            logger.info("Комната {} успешно добавлена", number);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при добавлении номера: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавлении номера: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        } finally {
            scanner.nextLine();
        }
    }
}