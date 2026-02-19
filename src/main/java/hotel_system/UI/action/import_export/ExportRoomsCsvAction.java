package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.model.ManagerHotel;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportRoomsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ExportRoomsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public ExportRoomsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало экспорта номеров в CSV");
        try {
            System.out.println("\n=== Экспорт номеров в CSV ===");
            System.out.print("Введите путь для сохранения файла (например: data/rooms_export.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                logger.warn("Введен пустой путь для экспорта");
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            logger.info("Экспорт номеров в файл: {}", filePath);
            manager.exportRoomsToCsv(filePath);

            System.out.println("\nЭкспорт успешно завершен! Файл сохранен: " + filePath);
            logger.info("Экспорт номеров успешно завершен, файл: {}", filePath);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при экспорте номеров: {}", e.getMessage(), e);
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