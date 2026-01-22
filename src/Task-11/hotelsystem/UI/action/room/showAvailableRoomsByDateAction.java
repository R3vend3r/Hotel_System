package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class showAvailableRoomsByDateAction implements Action {
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
        try {
            performAvailabilityCheck();
        } catch (Exception e) {
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
            throw new IllegalArgumentException("Дата должна быть в будущем");
        }
    }

    private void handleDateValidationError(Exception e) {
        if (e.getMessage().contains("Неверный формат даты") || e.getMessage().contains("Unparseable date")) {
            System.out.println("Ошибка: неверный формат даты. Используйте дд.мм.гг");
        } else if (e.getMessage().contains("Дата должна быть в будущем")) {
            System.out.println("Ошибка: дата должна быть в будущем");
        } else {
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
        System.out.println("Нет доступных номеров на эту дату");
    }

    private void printRoomsWithStatus(List<Room> availableRooms) {
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