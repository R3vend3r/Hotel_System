package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.model.ManagerHotel;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportAmenityOrdersCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ExportAmenityOrdersCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public ExportAmenityOrdersCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало экспорта заказов услуг в CSV");
        try {
            System.out.println("\n=== Экспорт заказов услуг в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/amenity_orders_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Экспорт заказов услуг в файл: {}", path);
            manager.exportAmenityOrdersToCsv(path);

            System.out.println("Экспорт заказов услуг завершен успешно!");
            logger.info("Экспорт заказов услуг успешно завершен, файл: {}", path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при экспорте заказов услуг: {}", e.getMessage(), e);
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