package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.controller.OrderController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataExportException;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportBookingsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ExportBookingsCsvAction.class);
    private final CsvTestController csvTestController;
    private final OrderController orderController;
    private final Scanner scanner = new Scanner(System.in);

    public ExportBookingsCsvAction(CsvTestController csvTestController, OrderController orderController) {
        this.csvTestController = csvTestController;
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.debug("Начало экспорта бронирований в CSV");
        try {
            System.out.println("\n=== Экспорт бронирований в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/bookings_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Экспорт бронирований в файл: {}", path);
            csvTestController.exportRoomBookingsToCsv(path);

            int activeBookings = orderController.getAllActiveBookings(SortType.NONE).size();
            int completedBookings = orderController.getAllCompletedBookings().size();
            int totalBookings = activeBookings + completedBookings;

            System.out.println("Успешно экспортировано бронирований: " + totalBookings);
            logger.info("Успешно экспортировано {} бронирований ({} активных, {} завершенных) в файл: {}",
                    totalBookings, activeBookings, completedBookings, path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при экспорте бронирований: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            logger.error("Ошибка экспорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта данных: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Системная ошибка при экспорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}