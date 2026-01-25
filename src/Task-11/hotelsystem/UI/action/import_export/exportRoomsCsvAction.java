package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class exportRoomsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(exportRoomsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportRoomsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("exportRoomsCsvAction: Начало экспорта номеров в CSV");
        try {
            System.out.println("\n=== Экспорт номеров в CSV ===");
            System.out.print("Введите путь для сохранения файла (например: data/rooms_export.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                logger.warn("exportRoomsCsvAction: Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            logger.info("exportRoomsCsvAction: Экспорт номеров в файл: {}", filePath);
            manager.exportRoomsToCsv(filePath);

            System.out.println("\nЭкспорт успешно завершен! Файл сохранен: " + filePath);
            logger.info("exportRoomsCsvAction: Экспорт номеров успешно завершен, файл: {}", filePath);

        } catch (IllegalArgumentException e) {
            logger.error("exportRoomsCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            logger.error("exportRoomsCsvAction: Ошибка экспорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("exportRoomsCsvAction: Системная ошибка при экспорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}