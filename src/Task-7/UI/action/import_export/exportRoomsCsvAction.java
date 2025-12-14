package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataExportException;

import java.util.Scanner;

public class exportRoomsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportRoomsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт номеров в CSV ===");
            System.out.print("Введите путь для сохранения файла (например: data/rooms_export.csv): ");
            String filePath = scanner.nextLine().trim();

            if (filePath.isEmpty()) {
                throw new IllegalArgumentException("Путь к файлу не может быть пустым");
            }

            manager.exportRoomsToCsv(filePath);
            System.out.println("\nЭкспорт успешно завершен! Файл сохранен: " + filePath);

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}