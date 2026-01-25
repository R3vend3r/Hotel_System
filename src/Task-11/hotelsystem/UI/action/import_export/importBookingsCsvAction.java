package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.RoomBooking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class importBookingsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(importBookingsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importBookingsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("importBookingsCsvAction: Начало импорта бронирований из CSV");
        try {
            System.out.println("\n=== Импорт бронирований из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/bookings_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("importBookingsCsvAction: Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("importBookingsCsvAction: Импорт бронирований из файла: {}", path);
            List<RoomBooking> imported = manager.importRoomBookingsFromCsv(path);

            System.out.println("Успешно импортировано бронирований: " + imported.size());
            logger.info("importBookingsCsvAction: Успешно импортировано {} бронирований из файла: {}",
                    imported.size(), path);

        } catch (IllegalArgumentException e) {
            logger.error("importBookingsCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            logger.error("importBookingsCsvAction: Ошибка импорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("importBookingsCsvAction: Системная ошибка при импорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}