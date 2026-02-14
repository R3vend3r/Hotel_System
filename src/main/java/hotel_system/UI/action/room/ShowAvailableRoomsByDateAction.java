package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.model.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ShowAvailableRoomsByDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAvailableRoomsByDateAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public ShowAvailableRoomsByDateAction(ManagerHotel manager) {
        this.manager = manager;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        logger.debug("Начало проверки доступности комнат по дате");
        try {
            System.out.print("\nПроверка доступности\nДата (дд.мм.гг): ");
            String dateString = scanner.nextLine();

            String formattedDateString = dateString.matches("\\d{2}\\.\\d{2}\\.\\d{2}")
                    ? dateString.substring(0, 6) + "20" + dateString.substring(6)
                    : dateString;

            Date targetDate = dateFormat.parse(formattedDateString);

            if (targetDate.before(new Date())) {
                throw new IllegalArgumentException("Дата должна быть в будущем");
            }

            logger.info("Проверка доступности на дату {}", dateString);

            System.out.println("\nДоступные номера:");
            List<Room> availableRooms = manager.getAvailableRoomsByDate(targetDate);

            if (availableRooms.isEmpty()) {
                logger.info("Нет доступных номеров на указанную дату");
                System.out.println("Нет доступных номеров на эту дату");
            } else {
                logger.info("Доступно {} номеров", availableRooms.size());
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");

                availableRooms.forEach(room -> {
                    String status = room.isAvailable()
                            ? "Свободен сейчас"
                            : "Освободится " + displayFormat.format(room.getAvailableDate());
                    System.out.printf("%d - %s (%s)%n",
                            room.getNumber(),
                            room.getType(),
                            status);
                });
            }

            logger.info("Проверка доступности завершена успешно");

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при проверке доступности: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка валидации даты: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при проверке доступности: {}", e.getMessage(), e);
            System.out.println("Ошибка: неверный формат даты. Используйте дд.мм.гг");
        }
    }
}