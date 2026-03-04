package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AddAmenityToClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(AddAmenityToClientAction.class);
    private final OrderController orderController;
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public AddAmenityToClientAction(OrderController orderController) {
        this.orderController = orderController;
        dateFormat.setLenient(false);
    }

    @Override
    public void execute() {
        logger.debug("Начало добавления услуги клиенту");
        try {
            System.out.println("\n=== Добавление услуги клиенту ===");

            System.out.print("Номер комнаты: ");
            int roomNumber;
            try {
                roomNumber = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                logger.warn("Введен некорректный номер комнаты");
                System.out.println("Ошибка: введите корректный номер комнаты");
                return;
            }

            System.out.print("ID услуги: ");
            String amenityId = scanner.nextLine().trim();

            if (amenityId.isEmpty()) {
                logger.warn("Введен пустой ID услуги");
                System.out.println("Ошибка: ID услуги не может быть пустым");
                return;
            }

            System.out.print("Дата оказания услуги (дд.мм.гг): ");
            String dateStr = scanner.nextLine().trim();

            Date serviceDate = parseAndValidateDate(dateStr);
            if (serviceDate == null) {
                return;
            }

            logger.info("Добавление услуги с ID '{}' в комнату {} на дату {}",
                    amenityId, roomNumber, serviceDate);

            AddAmenityRequest request = new AddAmenityRequest(
                    roomNumber,
                    amenityId,
                    serviceDate
            );

            orderController.addAmenityToClient(request);

            System.out.println("Услуга успешно добавлена!");
            logger.info("Услуга с ID '{}' успешно добавлена в комнату {}",
                    amenityId, roomNumber);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при добавлении услуги: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавлении услуги: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    private Date parseAndValidateDate(String dateStr) {
        if (dateStr.isEmpty()) {
            logger.warn("Введена пустая дата");
            System.out.println("Ошибка: дата не может быть пустой");
            return null;
        }
        try {
            String normalizedDateStr = normalizeDateString(dateStr);
            Date date = dateFormat.parse(normalizedDateStr);

            if (date.before(new Date())) {
                logger.warn("Введена прошедшая дата: {}", dateStr);
                System.out.println("Ошибка: дата должна быть в будущем");
                return null;
            }

            return date;
        } catch (ParseException e) {
            logger.warn("Некорректный формат даты: {}", dateStr);
            System.out.println("Ошибка: неверный формат даты. Используйте ДД.ММ.ГГ");
            return null;
        }
    }

    private String normalizeDateString(String dateStr) {
        if (dateStr.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            return dateStr.substring(0, 6) + "20" + dateStr.substring(6);
        }
        return dateStr;
    }
}