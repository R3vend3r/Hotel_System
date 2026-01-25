package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.Amenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class importAmenitiesCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(importAmenitiesCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importAmenitiesCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("importAmenitiesCsvAction: Начало импорта услуг из CSV");
        try {
            System.out.println("\n=== Импорт услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenities_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("importAmenitiesCsvAction: Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("importAmenitiesCsvAction: Импорт услуг из файла: {}", path);
            List<Amenity> imported = manager.importAmenitiesFromCsv(path);

            System.out.println("Успешно импортировано услуг: " + imported.size());
            logger.info("importAmenitiesCsvAction: Успешно импортировано {} услуг из файла: {}", imported.size(), path);

        } catch (IllegalArgumentException e) {
            logger.error("importAmenitiesCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            logger.error("importAmenitiesCsvAction: Ошибка импорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("importAmenitiesCsvAction: Системная ошибка при импорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}