package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.Exception.DataExportException;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class exportAmenitiesCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(exportAmenitiesCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportAmenitiesCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало экспорта услуг в CSV");
        try {
            System.out.println("\n=== Экспорт услуг в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/amenities_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Экспорт услуг в файл: {}", path);
            manager.exportAmenitiesToCsv(path);

            int exportedCount = manager.getAmenities(SortType.NONE).size();
            System.out.println("Успешно экспортировано услуг: " + exportedCount);
            logger.info("Успешно экспортировано {} услуг в файл: {}", exportedCount, path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при экспорте услуг: {}", e.getMessage(), e);
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