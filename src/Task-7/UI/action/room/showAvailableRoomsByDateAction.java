package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import model.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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
        displayAvailableRoomsForDate(targetDate);
    }

    private void printAvailabilityHeader() {
        System.out.print("\nПроверка доступности\nДата (дд.мм.гг): ");
    }

    private Date readAndValidateDate() {
        String dateString = readDateInput();
        String formattedDateString = formatDateString(dateString);
        Date date = parseDate(formattedDateString);
        validateDateIsInFuture(date);
        return date;
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

    private Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат даты");
        }
    }

    private void validateDateIsInFuture(Date date) {
        if (date.before(new Date())) {
            throw new IllegalArgumentException("Дата должна быть в будущем");
        }
    }

    private void displayAvailableRoomsForDate(Date targetDate) {
        printAvailableRoomsHeader();
        var availableRooms = manager.getAvailableRoomsByDate(targetDate);
        displayRoomsAvailability(availableRooms, targetDate);
    }

    private void printAvailableRoomsHeader() {
        System.out.println("\nДоступные номера:");
    }

    private void displayRoomsAvailability(Map<Integer, Room> availableRooms, Date targetDate) {
        if (availableRooms.isEmpty()) {
            handleNoAvailableRooms();
            return;
        }
        printRoomsWithStatus(availableRooms);
    }

    private void handleNoAvailableRooms() {
        System.out.println("Нет доступных номеров на эту дату");
    }

    private void printRoomsWithStatus(Map<Integer, Room> availableRooms) {
        availableRooms.values().forEach(this::printRoomWithStatus);
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
        if (e.getMessage().contains("Неверный формат даты")) {
            System.out.println("Используйте формат дд.мм.гг (например: 15.07.25)");
        }
    }
}