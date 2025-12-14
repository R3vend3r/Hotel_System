package UI.action.order;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;
import model.Client;
import model.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class settleClientAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public settleClientAction(ManagerHotel manager) {
        this.manager = manager;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        try {
            performSettlementProcess();
        } catch (IllegalArgumentException e) {
            handleInputError(e);
        } catch (NoSuchElementException e) {
            handleNotFoundError(e);
        } catch (IllegalStateException e) {
            handleOperationError(e);
        }catch (Exception e) {
            handleSettlementError(e);
        }
    }

    private void performSettlementProcess() {
        printSettlementHeader();
        Client client = createAndRegisterClient();
        Room room = selectAvailableRoom();
        Date checkOutDate = readCheckOutDate();
        confirmAndCompleteSettlement(client, room, checkOutDate);
    }

    private void printSettlementHeader() {
        System.out.println("\n=== Заселение клиента ===");
    }

    private Client createAndRegisterClient() {
        String name = readClientName();
        String surname = readClientSurname();
        validateClientData(name, surname);

        Client client = new Client(name, surname);
        manager.registerClient(client);
        return client;
    }

    private String readClientName() {
        System.out.print("Имя: ");
        return scanner.nextLine().trim();
    }

    private String readClientSurname() {
        System.out.print("Фамилия: ");
        return scanner.nextLine().trim();
    }

    private void validateClientData(String name, String surname) {
        if (name.isEmpty() || surname.isEmpty()) {
            throw new IllegalArgumentException("Имя и фамилия не могут быть пустыми");
        }
    }

    private Room selectAvailableRoom() {
        List<Room> availableRooms = getAvailableRooms();
        checkRoomAvailability(availableRooms);
        displayAvailableRooms(availableRooms);
        return chooseRoomFromList(availableRooms);
    }

    private List<Room> getAvailableRooms() {
        return manager.getRooms(SortType.NONE, true).values().stream()
                .filter(Room::isAvailable)
                .toList();
    }

    private void checkRoomAvailability(List<Room> availableRooms) {
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("Нет свободных номеров для заселения!");
        }
    }

    private void displayAvailableRooms(List<Room> availableRooms) {
        System.out.println("\nДоступные номера:");
        availableRooms.forEach(this::printRoomInfo);
    }

    private void printRoomInfo(Room room) {
        System.out.printf("%d - %s (%.2f руб.)%n",
                room.getNumberRoom(), room.getType(), room.getPriceForDay());
    }

    private Room chooseRoomFromList(List<Room> availableRooms) {
        System.out.print("\nНомер для заселения: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        return availableRooms.stream()
                .filter(r -> r.getNumberRoom() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Указан несуществующий или занятый номер"));
    }

    private Date readCheckOutDate() {
        System.out.print("Дата выезда (дд.мм.гг): ");
        String dateStr = scanner.nextLine();
        String formattedDateStr = formatDateString(dateStr);
        return parseAndValidateCheckOutDate(formattedDateStr);
    }

    private String formatDateString(String dateStr) {
        if (dateStr.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            return dateStr.substring(0, 6) + "20" + dateStr.substring(6);
        }
        return dateStr;
    }

    private Date parseAndValidateCheckOutDate(String dateStr) {
        try {
            Date checkOut = dateFormat.parse(dateStr);
            validateCheckOutDate(checkOut);
            return checkOut;
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат даты. Используйте дд.мм.гг (например: 15.07.25)");
        }
    }

    private void validateCheckOutDate(Date checkOut) {
        if (checkOut.before(new Date())) {
            throw new IllegalArgumentException("Дата выезда должна быть в будущем");
        }
    }

    private void confirmAndCompleteSettlement(Client client, Room room, Date checkOutDate) {
        displaySettlementConfirmation(client, room, checkOutDate);
        if (confirmSettlement()) {
            completeSettlement(client, room, checkOutDate);
        } else {
            cancelSettlement();
        }
    }

    private void displaySettlementConfirmation(Client client, Room room, Date checkOutDate) {
        System.out.printf("%nПодтвердите заселение:%n%s %s (ID: %s) в номер %d до %s%n",
                client.getName(), client.getSurname(), client.getId(),
                room.getNumberRoom(), new SimpleDateFormat("dd.MM.yyyy").format(checkOutDate));
    }

    private boolean confirmSettlement() {
        System.out.print("Подтвердить (да/нет)? ");
        String confirmation = scanner.nextLine();
        return confirmation.equalsIgnoreCase("да");
    }

    private void completeSettlement(Client client, Room room, Date checkOutDate) {
        manager.settleClient(client, room, checkOutDate);
        System.out.println("Клиент успешно заселен в номер " + room.getNumberRoom());
    }

    private void cancelSettlement() {
        System.out.println("Заселение отменено");
    }

    private void handleInputError(IllegalArgumentException e) {
        System.err.println("Ошибка ввода: " + e.getMessage());
    }

    private void handleNotFoundError(NoSuchElementException e) {
        System.err.println("Ошибка поиска: " + e.getMessage());
    }

    private void handleOperationError(IllegalStateException e){
        System.err.println("Ошибка операции: " + e.getMessage());
    }

    private void handleSettlementError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}