package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.ClientController;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportClientsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ExportClientsCsvAction.class);
    private final CsvTestController csvTestController;
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);

    public ExportClientsCsvAction(CsvTestController csvTestController, ClientController clientController) {
        this.csvTestController = csvTestController;
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало экспорта клиентов в CSV");
        try {
            System.out.println("\n=== Экспорт клиентов в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/clients_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Экспорт клиентов в файл: {}", path);
            csvTestController.exportClientsToCsv(path);

            int clientCount = clientController.getClientCount();
            System.out.println("Успешно экспортировано клиентов: " + clientCount);
            logger.info("Успешно экспортировано {} клиентов в файл: {}", clientCount, path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при экспорте клиентов: {}", e.getMessage(), e);
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