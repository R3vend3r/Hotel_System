package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataExportException;
import java.util.Scanner;

public class exportAmenityOrdersCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportAmenityOrdersCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт заказов услуг в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/amenity_orders_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            manager.exportAmenityOrdersToCsv(path);
            System.out.println("Экспорт заказов услуг завершен успешно!");
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}