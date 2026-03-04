package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Amenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Scanner;

public class ImportAmenitiesCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ImportAmenitiesCsvAction.class);
    private final CsvTestController csvTestController;
    private final Scanner scanner = new Scanner(System.in);

    public ImportAmenitiesCsvAction(CsvTestController csvTestController) {
        this.csvTestController = csvTestController;
    }

    @Override
    public void execute() {
        logger.debug("Начало импорта услуг из CSV");
        try {
            System.out.println("\n=== Импорт услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenities_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Импорт услуг из файла: {}", path);
            ResponseEntity<List<Amenity>> response = csvTestController.importAmenitiesFromCsv(path);
            List<Amenity> imported = response.getBody();
            assert imported != null;
            System.out.println("Успешно импортировано услуг: " + imported.size());
            logger.info("Успешно импортировано {} услуг из файла: {}", imported.size(), path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при импорте услуг: {}", e.getMessage(), e);
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