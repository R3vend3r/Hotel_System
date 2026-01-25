package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class showAvailableRoomsByDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAvailableRoomsByDateAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public showAvailableRoomsByDateAction(ManagerHotel manager) {
        this.manager = manager;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        logger.info("showAvailableRoomsByDateAction: Начало проверки доступности комнат по дате");
        try {
            performAvailabilityCheck();
            logger.info("showAvailableRoomsByDateAction: Проверка доступности завершена успешно");
        } catch (Exception e) {
            logger.error("showAvailableRoomsByDateAction: Ошибка при проверке доступности: {}", e.getMessage(), e);
            handleAvailabilityCheckError(e);
        }
    }

    private void performAvailabilityCheck() {
        printAvailabilityHeader();
        Date targetDate = readAndValidateDate();
        if (targetDate == null) return;
        displayAvailableRoomsForDate(targetDate);
    }

    private void printAvailabilityHeader() {
        System.out.print("\nПроверка доступности\nДата (дд.мм.гг): ");
    }

    private Date readAndValidateDate() {
        try {
            String dateString = readDateInput();
            String formattedDateString = formatDateString(dateString);
            Date date = parseDate(formattedDateString);
            validateDateIsInFuture(date);

            logger.info("showAvailableRoomsByDateAction: Проверка доступности на дату {}", dateString);
            return date;

        } catch (Exception e) {
            handleDateValidationError(e);
            return null;
        }
    }

    private String readDateInput() {
        return scanner.nextLine();
    }

    private String formatDateString(String dateString) {
        if (dateString.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            return dateString.substring(0, 6) + "20" + dateString.substring(6);
        }
        return dateString;
    }

    private Date parseDate(String dateString) throws Exception {
        return dateFormat.parse(dateString);
    }

    private void validateDateIsInFuture(Date date) {
        if (date.before(new Date())) {
            logger.warn("showAvailableRoomsByDateAction: Введена прошедшая дата");
            throw new IllegalArgumentException("Дата должна быть в будущем");
        }
    }

    private void handleDateValidationError(Exception e) {
        if (e.getMessage().contains("Неверный формат даты") || e.getMessage().contains("Unparseable date")) {
            logger.error("showAvailableRoomsByDateAction: Неверный формат даты");
            System.out.println("Ошибка: неверный формат даты. Используйте дд.мм.гг");
        } else if (e.getMessage().contains("Дата должна быть в будущем")) {
            logger.warn("showAvailableRoomsByDateAction: Введена прошедшая дата");
            System.out.println("Ошибка: дата должна быть в будущем");
        } else {
            logger.error("showAvailableRoomsByDateAction: Ошибка валидации даты: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void displayAvailableRoomsForDate(Date targetDate) {
        printAvailableRoomsHeader();
        List<Room> availableRooms = manager.getAvailableRoomsByDate(targetDate);
        displayRoomsAvailability(availableRooms);
    }

    private void printAvailableRoomsHeader() {
        System.out.println("\nДоступные номера:");
    }

    private void displayRoomsAvailability(List<Room> availableRooms) {
        if (availableRooms.isEmpty()) {
            handleNoAvailableRooms();
            return;
        }
        printRoomsWithStatus(availableRooms);
    }

    private void handleNoAvailableRooms() {
        logger.info("showAvailableRoomsByDateAction: Нет доступных номеров на указанную дату");
        System.out.println("Нет доступных номеров на эту дату");
    }

    private void printRoomsWithStatus(List<Room> availableRooms) {
        logger.info("showAvailableRoomsByDateAction: Доступно {} номеров", availableRooms.size());
        availableRooms.forEach(this::printRoomWithStatus);
    }

    private void printRoomWithStatus(Room room) {
        String status = determineRoomStatus(room);
        System.out.printf("%d - %s (%s)%n",
                room.getNumberRoom(),
                room.getType(),
                status);
    }

    private String determineRoomStatus(Room room) {
        if (room.isAvailable()) {
            return "Свободен сейчас";
        } else {
            return "Освободится " + displayDateFormat.format(room.getAvailableDate());
        }
    }

    private void handleAvailabilityCheckError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}