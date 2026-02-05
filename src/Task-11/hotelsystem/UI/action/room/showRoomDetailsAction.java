package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class showRoomDetailsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showRoomDetailsAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showRoomDetailsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showRoomDetailsAction: Начало отображения деталей комнаты");
        try {
            displayRoomDetails();
            logger.info("showRoomDetailsAction: Отображение деталей комнаты завершено успешно");
        } catch (InputMismatchException e) {
            logger.error("showRoomDetailsAction: Ошибка ввода - номер комнаты должен быть целым числом: {}", e.getMessage());
            handleInputMismatchError(e);
        } catch (IllegalArgumentException e) {
            logger.error("showRoomDetailsAction: Ошибка аргумента: {}", e.getMessage());
            handleIllegalArgumentError(e);
        } catch (Exception e) {
            logger.error("showRoomDetailsAction: Неожиданная ошибка при отображении деталей комнаты: {}", e.getMessage(), e);
            handleUnexpectedError(e);
        }
    }

    private void displayRoomDetails() {
        int roomNumber = readRoomNumber();
        Optional<Room> roomDetails = getRoomDetailsFromManager(roomNumber);
        printRoomDetails(roomDetails);
    }

    private int readRoomNumber() {
        System.out.print("\nВведите номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        logger.info("showRoomDetailsAction: Запрос деталей для комнаты {}", roomNumber);
        return roomNumber;
    }

    private Optional<Room> getRoomDetailsFromManager(int roomNumber) {
        return manager.findRoom(roomNumber);
    }

    private void printRoomDetails(Optional<Room> roomDetails) {
        if (roomDetails.isPresent()) {
            System.out.println(roomDetails.get());
            logger.info("showRoomDetailsAction: Детали комнаты {} успешно отображены", roomDetails.get().getNumberRoom());
        } else {
            System.out.println("Комната не найдена");
            logger.warn("showRoomDetailsAction: Комната не найдена");
        }
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