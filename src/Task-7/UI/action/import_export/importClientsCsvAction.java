package UI.action.import_export;


import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataImportException;
import model.Client;

import java.util.List;
import java.util.Scanner;

public class importClientsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importClientsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт клиентов из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/clients_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            List<Client> imported = manager.importClientsFromCsv(path);
            System.out.println("Успешно импортировано клиентов: " + imported.size());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}