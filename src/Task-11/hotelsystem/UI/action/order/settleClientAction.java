package hotelsystem.UI.action.order;

import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import hotelsystem.model.Client;
import hotelsystem.controller.ManagerHotel;
import hotelsystem.model.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.sql.SQLException;

public class settleClientAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public settleClientAction(ManagerHotel manager) {
        this.manager = manager;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        try {
            processSettlement();
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println("Ошибка выбора: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Ошибка формата даты: используйте дд.мм.гг (например: 15.07.25)");
        } catch (IllegalStateException e) {
            System.err.println("Ошибка операции: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка базы данных: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        } finally {
            scanner.nextLine();
        }
    }

    private void processSettlement() throws ParseException, SQLException {
        System.out.println("\n=== Заселение клиента ===");

        Client client = createAndRegisterClient();
        if (client == null) return;

        Room room = selectAvailableRoom();
        if (room == null) return;

        Date checkOutDate = readAndValidateCheckOutDate();
        if (checkOutDate == null) return;

        confirmAndCompleteSettlement(client, room, checkOutDate);
    }

    private Client createAndRegisterClient() throws SQLException {
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Фамилия: ");
        String surname = scanner.nextLine().trim();

        if (isNameInvalid(name, surname)) {
            System.out.println("Ошибка: имя и фамилия не могут быть пустыми");
            return null;
        }

        Client client = new Client(name, surname);
        manager.registerClient(client);
        return client;
    }

    private boolean isNameInvalid(String name, String surname) {
        return name.isEmpty() || surname.isEmpty();
    }

    private Room selectAvailableRoom() throws SQLException {
        List<Room> availableRooms = getAvailableRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("\nНет свободных номеров для заселения!");
            return null;
        }

        displayAvailableRooms(availableRooms);
        return readAndValidateRoomSelection(availableRooms);
    }

    private List<Room> getAvailableRooms() throws SQLException {
        return manager.getRooms(SortType.NONE, true)
                .stream()
                .filter(Room::isAvailable)
                .toList();
    }

    private void displayAvailableRooms(List<Room> rooms) {
        System.out.println("\nДоступные номера:");
        rooms.forEach(this::displayRoomInfo);
    }

    private void displayRoomInfo(Room room) {
        System.out.printf("%d - %s (%.2f руб.)%n",
                room.getNumberRoom(), room.getType(), room.getPriceForDay());
    }

    private Room readAndValidateRoomSelection(List<Room> availableRooms) {
        System.out.print("\nНомер для заселения: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room room = findRoomByNumber(availableRooms, roomNumber);
        if (room == null) {
            System.out.println("Ошибка: указан несуществующий или занятый номер");
        }

        return room;
    }

    private Room findRoomByNumber(List<Room> rooms, int roomNumber) {
        return rooms.stream()
                .filter(r -> r.getNumberRoom() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    private Date readAndValidateCheckOutDate() throws ParseException {
        System.out.print("Дата выезда (дд.мм.гг): ");
        String dateStr = scanner.nextLine();

        String normalizedDateStr = normalizeDateString(dateStr);
        Date checkOutDate = dateFormat.parse(normalizedDateStr);

        if (!isFutureDate(checkOutDate)) {
            System.out.println("Ошибка: дата выезда должна быть в будущем");
            return null;
        }

        return checkOutDate;
    }

    private String normalizeDateString(String dateStr) {
        if (dateStr.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            return dateStr.substring(0, 6) + "20" + dateStr.substring(6);
        }
        return dateStr;
    }

    private boolean isFutureDate(Date date) {
        return date.after(new Date());
    }

    private void confirmAndCompleteSettlement(Client client, Room room, Date checkOutDate) throws SQLException {
        displaySettlementConfirmation(client, room, checkOutDate);

        if (isConfirmationReceived()) {
            completeSettlement(client, room, checkOutDate);
        } else {
            cancelSettlement();
        }
    }

    private void displaySettlementConfirmation(Client client, Room room, Date checkOutDate) {
        System.out.printf("%nПодтвердите заселение:%n%s %s (ID: %s) в номер %d до %s%n",
                client.getName(), client.getSurname(), client.getId(),
                room.getNumberRoom(), displayDateFormat.format(checkOutDate));
        System.out.print("Подтвердить (да/нет)? ");
    }

    private boolean isConfirmationReceived() {
        String confirmation = scanner.nextLine();
        return confirmation.equalsIgnoreCase("да");
    }

    private void completeSettlement(Client client, Room room, Date checkOutDate) throws SQLException {
        manager.settleClient(client, room, checkOutDate);
        System.out.println("Клиент успешно заселен в номер " + room.getNumberRoom());
    }

    private void cancelSettlement() {
        System.out.println("Заселение отменено");
    }
}