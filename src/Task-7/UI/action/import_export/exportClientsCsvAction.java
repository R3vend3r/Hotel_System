package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataExportException;
import java.util.Scanner;

public class exportClientsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportClientsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт клиентов в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/clients_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            manager.exportClientsToCsv(path);
            System.out.println("Успешно экспортировано клиентов: " + manager.getClientCount());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}