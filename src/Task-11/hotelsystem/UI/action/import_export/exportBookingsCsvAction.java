package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataExportException;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class exportBookingsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(exportBookingsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportBookingsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("exportBookingsCsvAction: Начало экспорта бронирований в CSV");
        try {
            System.out.println("\n=== Экспорт бронирований в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/bookings_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("exportBookingsCsvAction: Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("exportBookingsCsvAction: Экспорт бронирований в файл: {}", path);
            manager.exportRoomBookingsToCsv(path);

            int activeBookings = manager.getAllActiveBookings(SortType.NONE).size();
            int completedBookings = manager.getAllCompletedBookings().size();
            int totalBookings = activeBookings + completedBookings;

            System.out.println("Успешно экспортировано бронирований: " + totalBookings);
            logger.info("exportBookingsCsvAction: Успешно экспортировано {} бронирований ({} активных, {} завершенных) в файл: {}",
                    totalBookings, activeBookings, completedBookings, path);

        } catch (IllegalArgumentException e) {
            logger.error("exportBookingsCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            logger.error("exportBookingsCsvAction: Ошибка экспорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("exportBookingsCsvAction: Системная ошибка при экспорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}