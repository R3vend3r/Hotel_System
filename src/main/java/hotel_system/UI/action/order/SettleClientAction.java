package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.UI.action.Action;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.controller.RoomController;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SettleClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(SettleClientAction.class);
    private final RoomController roomController;
    private final OrderController orderController;
    private final ClientController clientController;


    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public SettleClientAction(RoomController roomController, OrderController orderController, ClientController clientController) {
        this.roomController = roomController;
        this.orderController = orderController;
        this.clientController = clientController;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        logger.debug("Начало процесса заселения клиента");
        try {
            processSettlement();
            logger.info("Процесс заселения завершен успешно");
        } catch (ManagerHotelException e) {
            logger.error("Ошибка при заселении клиента: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при заселении: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    private void processSettlement() throws Exception {
        System.out.println("\n=== Заселение клиента ===");

        Client client = createClient();
        if (client == null) {
            logger.warn("Создание клиента прервано");
            return;
        }

        Room room = selectAvailableRoom();
        if (room == null) {
            logger.warn("Выбор комнаты прерван");
            return;
        }

        Date checkOutDate = readAndValidateCheckOutDate();
        if (checkOutDate == null) {
            logger.warn("Ввод даты выезда прерван");
            return;
        }

        confirmAndCompleteSettlement(client, room, checkOutDate);
    }

    private Client createClient() {
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Фамилия: ");
        String surname = scanner.nextLine().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            logger.warn("Введены пустые имя или фамилия");
            System.out.println("Ошибка: имя и фамилия не могут быть пустыми");
            return null;
        }

        logger.info("Регистрация нового клиента: {} {}", name, surname);
        return new Client(name, surname);
    }

    private Room selectAvailableRoom() {
        List<Room> availableRooms = roomController.getRooms(SortType.NONE, true)
                .stream()
                .filter(Room::isAvailable)
                .toList();

        if (availableRooms.isEmpty()) {
            logger.warn("Нет доступных комнат для заселения");
            System.out.println("Нет свободных номеров для заселения!");
            return null;
        }

        System.out.println("\nДоступные номера:");
        availableRooms.forEach(room ->
                System.out.printf("%d - %s (%.2f руб.)%n",
                        room.getNumber(), room.getType(), room.getPriceForDay())
        );

        System.out.print("\nНомер для заселения: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        logger.info("Пользователь выбрал комнату {}", roomNumber);
        return availableRooms.stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    private Date readAndValidateCheckOutDate() throws Exception {
        System.out.print("Дата выезда (дд.мм.гг): ");
        String dateStr = scanner.nextLine();

        logger.info("Введена дата выезда: {}", dateStr);
        String normalizedDateStr = normalizeDateString(dateStr);
        Date checkOutDate = dateFormat.parse(normalizedDateStr);

        if (!checkOutDate.after(new Date())) {
            logger.warn("Введена прошедшая дата: {}", dateStr);
            System.out.println("Ошибка: дата выезда должна быть в будущем");
            return null;
        }

        logger.info("Дата выезда валидна: {}", checkOutDate);
        return checkOutDate;
    }

    private String normalizeDateString(String dateStr) {
        if (dateStr.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            return dateStr.substring(0, 6) + "20" + dateStr.substring(6);
        }
        return dateStr;
    }

    private void confirmAndCompleteSettlement(Client client, Room room, Date checkOutDate) throws Exception {
        System.out.printf("%nПодтвердите заселение:%n%s %s (ID: %s) в номер %d до %s%n",
                client.getName(), client.getSurname(), client.getId(),
                room.getNumber(), new SimpleDateFormat("dd.MM.yyyy").format(checkOutDate));
        System.out.print("Подтвердить (да/нет)? ");

        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("да")) {
            logger.info("Заселение клиента {} в комнату {} до {}",
                    client.getId(), room.getNumber(), checkOutDate);
            clientController.registerClient(client);
            orderController.settleClient(client, room, checkOutDate);
            System.out.println("Клиент успешно заселен в номер " + room.getNumber());
            logger.info("Клиент {} успешно заселен в комнату {}",
                    client.getId(), room.getNumber());
        } else {
            logger.info("Пользователь отменил заселение");
            System.out.println("Заселение отменено");
        }
    }
}