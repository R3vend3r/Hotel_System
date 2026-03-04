package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.UI.action.Action;
import hotel_system.controller.ClientController;
import hotel_system.controller.OrderController;
import hotel_system.controller.RoomController;
import hotel_system.dto.ClientResponse;
import hotel_system.dto.RoomResponse;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.enums.SortType;
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

        // 1. Поиск существующего клиента по ID
        ClientResponse client = findExistingClient();
        if (client == null) {
            logger.warn("Поиск клиента прерван");
            return;
        }

        // 2. Проверка, не заселен ли уже клиент
        if (client.roomNumber() != null && client.roomNumber() > 0) {
            System.out.printf("Клиент %s %s уже заселен в комнату %d%n",
                    client.name(), client.surname(), client.roomNumber());
            System.out.print("Переселить в другую комнату (да/нет)? ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("да")) {
                logger.info("Пользователь отменил переселение клиента {}", client.id());
                return;
            }
        }

        // 3. Выбор свободной комнаты
        RoomResponse room = selectAvailableRoom();
        if (room == null) {
            logger.warn("Выбор комнаты прерван");
            return;
        }

        // 4. Ввод даты выезда
        Date checkOutDate = readAndValidateCheckOutDate();
        if (checkOutDate == null) {
            logger.warn("Ввод даты выезда прерван");
            return;
        }

        // 5. Подтверждение и заселение
        confirmAndCompleteSettlement(client, room, checkOutDate);
    }

    private ClientResponse findExistingClient() {
        System.out.println("Поиск существующего клиента:");
        System.out.print("Введите ID клиента: ");
        String clientId = scanner.nextLine().trim();

        if (clientId.isEmpty()) {
            logger.warn("Введен пустой ID клиента");
            System.out.println("Ошибка: ID клиента не может быть пустым");
            return null;
        }

        logger.info("Поиск клиента по ID: {}", clientId);
        return clientController.findClientById(clientId)
                .orElseGet(() -> {
                    System.out.println("Клиент с ID " + clientId + " не найден");
                    return null;
                });
    }

    private RoomResponse selectAvailableRoom() {
        List<RoomResponse> availableRooms = roomController.getRooms(SortType.NONE, true);

        if (availableRooms.isEmpty()) {
            logger.warn("Нет доступных комнат для заселения");
            System.out.println("Нет свободных номеров для заселения!");
            return null;
        }

        System.out.println("\nДоступные номера:");
        availableRooms.forEach(room ->
                System.out.printf("%d - %s (%.2f руб.)%n",
                        room.number(), room.type(), room.price())
        );

        System.out.print("\nНомер для заселения: ");
        int roomNumber;
        try {
            roomNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            logger.warn("Введен некорректный номер комнаты");
            System.out.println("Ошибка: введите корректный номер");
            return null;
        }

        logger.info("Пользователь выбрал комнату {}", roomNumber);

        return availableRooms.stream()
                .filter(r -> r.number() == roomNumber)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Комната " + roomNumber + " не найдена в списке доступных");
                    return null;
                });
    }

    private Date readAndValidateCheckOutDate() throws Exception {
        System.out.print("Дата выезда (дд.мм.гг): ");
        String dateStr = scanner.nextLine().trim();

        if (dateStr.isEmpty()) {
            logger.warn("Введена пустая дата");
            System.out.println("Ошибка: дата не может быть пустой");
            return null;
        }

        logger.info("Введена дата выезда: {}", dateStr);
        String normalizedDateStr = normalizeDateString(dateStr);

        Date checkOutDate;
        try {
            checkOutDate = dateFormat.parse(normalizedDateStr);
        } catch (Exception e) {
            logger.warn("Некорректный формат даты: {}", dateStr);
            System.out.println("Ошибка: неверный формат даты. Используйте ДД.ММ.ГГ");
            return null;
        }

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

    private void confirmAndCompleteSettlement(ClientResponse client, RoomResponse room, Date checkOutDate) throws Exception {
        System.out.printf("%nПодтвердите заселение:%n%s %s (ID: %s) в номер %d до %s%n",
                client.name(), client.surname(), client.id(),
                room.number(), new SimpleDateFormat("dd.MM.yyyy").format(checkOutDate));
        System.out.print("Подтвердить (да/нет)? ");

        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("да")) {
            logger.info("Заселение клиента {} в комнату {} до {}",
                    client.id(), room.number(), checkOutDate);

            SettleClientRequest request = new SettleClientRequest(
                    client.id(),
                    room.number(),
                    checkOutDate
            );

            orderController.settleClient(request);

            System.out.println("Клиент успешно заселен в номер " + room.number());
            logger.info("Клиент {} успешно заселен в комнату {}",
                    client.id(), room.number());
        } else {
            logger.info("Пользователь отменил заселение клиента {}", client.id());
            System.out.println("Заселение отменено");
        }
    }
}