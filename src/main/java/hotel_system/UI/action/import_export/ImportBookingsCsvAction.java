package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.RoomBooking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Scanner;

public class ImportBookingsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ImportBookingsCsvAction.class);
    private final CsvTestController csvTestController;
    private final Scanner scanner = new Scanner(System.in);

    public ImportBookingsCsvAction(CsvTestController csvTestController) {
        this.csvTestController = csvTestController;
    }

    @Override
    public void execute() {
        logger.debug("Начало импорта бронирований из CSV");
        try {
            System.out.println("\n=== Импорт бронирований из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/bookings_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Импорт бронирований из файла: {}", path);
            ResponseEntity<List<RoomBooking>> response = csvTestController.importRoomBookingsFromCsv(path);
            List<RoomBooking> imported = response.getBody();
            assert imported != null;
            System.out.println("Успешно импортировано бронирований: " + imported.size());
            logger.info("Успешно импортировано {} бронирований из файла: {}", imported.size(), path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при импорте бронирований: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            logger.error("Ошибка импорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта данных: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Системная ошибка при импорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}