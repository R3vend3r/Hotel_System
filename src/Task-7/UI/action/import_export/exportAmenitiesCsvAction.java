package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataExportException;
import enums.SortType;

import java.util.Scanner;

public class exportAmenitiesCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportAmenitiesCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт услуг в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/amenities_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            manager.exportAmenitiesToCsv(path);
            System.out.println("Успешно экспортировано услуг: " + manager.getAmenities(SortType.NONE).size());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}