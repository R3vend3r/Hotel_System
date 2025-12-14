package hotelsystem.UI.action.import_export;


import hotelsystem.UI.action.Action;
import hotelsystem.controller.ManagerHotel;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.Amenity;

import java.util.List;
import java.util.Scanner;

public class importAmenitiesCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importAmenitiesCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenities_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            List<Amenity> imported = manager.importAmenitiesFromCsv(path);
            System.out.println("Успешно импортировано услуг: " + imported.size());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}