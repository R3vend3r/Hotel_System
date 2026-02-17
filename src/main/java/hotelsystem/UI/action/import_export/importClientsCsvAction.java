package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class importClientsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(importClientsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importClientsCsvAction(ManagerHotel manager) {
        this.manager = manager;
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
            List<Client> imported = manager.importClientsFromCsv(path);

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