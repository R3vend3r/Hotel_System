package hotel_system.UI.action.import_export;

import hotel_system.UI.action.Action;
import hotel_system.controller.CsvTestController;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportRoomsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ImportRoomsCsvAction.class);
    private final CsvTestController csvTestController;
    private final Scanner scanner = new Scanner(System.in);

    public ImportRoomsCsvAction(CsvTestController csvTestController) {
        this.csvTestController = csvTestController;
    }

    @Override
    public void execute() {
        logger.debug("Начало импорта номеров из CSV");
        try {
            System.out.println("\n=== Импорт номеров из CSV ===");
            System.out.print("Введите путь к файлу (например: data/rooms_import.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                logger.warn("Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            logger.info("Импорт номеров из файла: {}", filePath);
            List<Room> importedRooms = csvTestController.importRoomsFromCsv(filePath);

            System.out.println("\nИмпорт успешно завершен! Загружено номеров: " + importedRooms.size());
            logger.info("Успешно импортировано {} номеров из файла: {}",
                    importedRooms.size(), filePath);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при импорте номеров: {}", e.getMessage(), e);
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