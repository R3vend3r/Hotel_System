package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.AmenityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class importAmenityOrdersCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(importAmenityOrdersCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importAmenityOrdersCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("importAmenityOrdersCsvAction: Начало импорта заказов услуг из CSV");
        try {
            System.out.println("\n=== Импорт заказов услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenity_orders_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("importAmenityOrdersCsvAction: Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("importAmenityOrdersCsvAction: Импорт заказов услуг из файла: {}", path);
            List<AmenityOrder> imported = manager.importAmenityOrdersFromCsv(path);

            System.out.println("Успешно импортировано заказов: " + imported.size());
            logger.info("importAmenityOrdersCsvAction: Успешно импортировано {} заказов услуг из файла: {}",
                    imported.size(), path);

        } catch (IllegalArgumentException e) {
            logger.error("importAmenityOrdersCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            logger.error("importAmenityOrdersCsvAction: Ошибка импорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("importAmenityOrdersCsvAction: Системная ошибка при импорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}