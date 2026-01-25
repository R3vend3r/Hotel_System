package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class exportAmenityOrdersCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(exportAmenityOrdersCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportAmenityOrdersCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("exportAmenityOrdersCsvAction: Начало экспорта заказов услуг в CSV");
        try {
            System.out.println("\n=== Экспорт заказов услуг в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/amenity_orders_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("exportAmenityOrdersCsvAction: Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("exportAmenityOrdersCsvAction: Экспорт заказов услуг в файл: {}", path);
            manager.exportAmenityOrdersToCsv(path);

            System.out.println("Экспорт заказов услуг завершен успешно!");
            logger.info("exportAmenityOrdersCsvAction: Экспорт заказов услуг успешно завершен, файл: {}", path);

        } catch (IllegalArgumentException e) {
            logger.error("exportAmenityOrdersCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            logger.error("exportAmenityOrdersCsvAction: Ошибка экспорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("exportAmenityOrdersCsvAction: Системная ошибка при экспорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}