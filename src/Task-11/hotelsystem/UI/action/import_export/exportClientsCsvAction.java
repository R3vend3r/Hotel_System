package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class exportClientsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(exportClientsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportClientsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("exportClientsCsvAction: Начало экспорта клиентов в CSV");
        try {
            System.out.println("\n=== Экспорт клиентов в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/clients_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("exportClientsCsvAction: Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("exportClientsCsvAction: Экспорт клиентов в файл: {}", path);
            manager.exportClientsToCsv(path);

            int clientCount = manager.getClientCount();
            System.out.println("Успешно экспортировано клиентов: " + clientCount);
            logger.info("exportClientsCsvAction: Успешно экспортировано {} клиентов в файл: {}", clientCount, path);

        } catch (IllegalArgumentException e) {
            logger.error("exportClientsCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            logger.error("exportClientsCsvAction: Ошибка экспорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("exportClientsCsvAction: Системная ошибка при экспорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}