package UI.action.import_export;


import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataImportException;
import model.AmenityOrder;

import java.util.List;
import java.util.Scanner;

public class importAmenityOrdersCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importAmenityOrdersCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт заказов услуг из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/amenity_orders_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            List<AmenityOrder> imported = manager.importAmenityOrdersFromCsv(path);
            System.out.println("Успешно импортировано заказов: " + imported.size());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}
