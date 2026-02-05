package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.model.ManagerHotel;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class importRoomsCsvAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(importRoomsCsvAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importRoomsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("importRoomsCsvAction: Начало импорта номеров из CSV");
        try {
            System.out.println("\n=== Импорт номеров из CSV ===");
            System.out.print("Введите путь к файлу (например: data/rooms_import.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                logger.warn("importRoomsCsvAction: Введен пустой путь для импорта");
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            logger.info("importRoomsCsvAction: Импорт номеров из файла: {}", filePath);
            List<Room> importedRooms = manager.importRoomsFromCsv(filePath);

            System.out.println("\nИмпорт успешно завершен! Загружено номеров: " + importedRooms.size());
            logger.info("importRoomsCsvAction: Успешно импортировано {} номеров из файла: {}",
                    importedRooms.size(), filePath);

        } catch (IllegalArgumentException e) {
            logger.error("importRoomsCsvAction: Ошибка ввода данных: {}", e.getMessage());
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            logger.error("importRoomsCsvAction: Ошибка импорта данных: {}", e.getMessage(), e);
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("importRoomsCsvAction: Системная ошибка при импорте: {}", e.getMessage(), e);
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}