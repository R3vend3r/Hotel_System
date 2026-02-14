package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.model.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class ShowRoomDetailsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowRoomDetailsAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public ShowRoomDetailsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения деталей комнаты");
        try {
            System.out.print("\nВведите номер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Запрос деталей для комнаты {}", roomNumber);
            Optional<Room> roomDetails = manager.findRoom(roomNumber);

            if (roomDetails.isPresent()) {
                System.out.println(roomDetails.get());
                logger.info("Детали комнаты {} успешно отображены", roomDetails.get().getNumber());
            } else {
                System.out.println("Комната не найдена");
                logger.warn("Комната не найдена");
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