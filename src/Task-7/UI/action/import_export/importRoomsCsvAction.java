package UI.action.import_export;


import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataImportException;
import model.Room;

import java.util.List;
import java.util.Scanner;

public class importRoomsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importRoomsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт номеров из CSV ===");
            System.out.print("Введите путь к файлу (например: data/rooms_import.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            List<Room> importedRooms = manager.importRoomsFromCsv(filePath);
            System.out.println("\nИмпорт успешно завершен! Загружено номеров: " + importedRooms.size());

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}