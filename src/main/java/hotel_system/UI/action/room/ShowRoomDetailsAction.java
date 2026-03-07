package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.RoomController;
import hotel_system.UI.action.Action;
import hotel_system.dto.RoomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class ShowRoomDetailsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomDetailsAction.class);
    private final RoomController controller;
    private final Scanner scanner = new Scanner(System.in);

    public ShowRoomDetailsAction(RoomController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения деталей комнаты");
        try {
            System.out.print("\nВведите номер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Запрос деталей для комнаты {}", roomNumber);
            Optional<RoomResponse> roomDetails = controller.findRoom(roomNumber);

            if (roomDetails.isPresent()) {
                RoomResponse room = roomDetails.get();
                System.out.println("\n=== ДЕТАЛИ КОМНАТЫ ===");
                System.out.println("Номер: " + room.number());
                System.out.println("Тип: " + room.type());
                System.out.println("Цена за день: " + room.price() + " руб.");
                System.out.println("Вместимость: " + room.capacity() + " чел.");
                System.out.println("Звезд: " + room.stars());
                System.out.println("Состояние: " + room.roomCondition());
                System.out.println("Доступна: " + (room.isAvailable() ? "Да" : "Нет"));
                logger.info("Детали комнаты {} успешно отображены", room.number());
            } else {
                System.out.println("Комната с номером " + roomNumber + " не найдена");
                logger.warn("Комната {} не найдена", roomNumber);
            }

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении деталей комнаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InputMismatchException e) {
            logger.error("Ошибка ввода - номер комнаты должен быть целым числом");
            System.out.println("Ошибка: Номер комнаты должен быть целым числом");
            scanner.nextLine();
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении деталей комнаты: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}