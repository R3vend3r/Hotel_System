package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.AmenityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Scanner;

public class ImportAmenityOrdersCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ImportAmenityOrdersCsvAction.class);
    private final CsvTestController csvTestController;
    private final Scanner scanner = new Scanner(System.in);

    public ImportAmenityOrdersCsvAction(CsvTestController csvTestController) {
        this.csvTestController = csvTestController;
    }

    @Override
    public void execute() {
        logger.debug("Начало импорта заказов услуг из CSV");
        try {
            System.out.println("\n=== Импорт заказов услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenity_orders_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Импорт заказов услуг из файла: {}", path);
            ResponseEntity<List<AmenityOrder>> response = csvTestController.importAmenityOrdersFromCsv(path);
            List<AmenityOrder> imported = response.getBody();
            assert imported != null;
            System.out.println("Успешно импортировано заказов: " + imported.size());
            logger.info("Успешно импортировано {} заказов услуг из файла: {}",
                    imported.size(), path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при импорте заказов услуг: {}", e.getMessage(), e);
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