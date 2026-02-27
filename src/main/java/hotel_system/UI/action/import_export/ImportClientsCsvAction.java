package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportClientsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ImportClientsCsvAction.class);
    private final CsvTestController csvTestController;
    private final Scanner scanner = new Scanner(System.in);

    public ImportClientsCsvAction(CsvTestController csvTestController) {
        this.csvTestController = csvTestController;
    }

    @Override
    public void execute() {
        logger.debug("Начало импорта клиентов из CSV");
        try {
            System.out.println("\n=== Импорт клиентов из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/clients_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) {
                logger.warn("Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь не может быть пустым");
            }

            logger.info("Импорт клиентов из файла: {}", path);
            List<Client> imported = csvTestController.importClientsFromCsv(path);

            System.out.println("Успешно импортировано клиентов: " + imported.size());
            logger.info("Успешно импортировано {} клиентов из файла: {}",
                    imported.size(), path);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при импорте клиентов: {}", e.getMessage(), e);
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