package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Exception.DataImportException;
import model.RoomBooking;

import java.util.List;
import java.util.Scanner;

public class importBookingsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public importBookingsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт бронирований из CSV ===");
            System.out.print("Введите путь к файлу (пример: data/bookings_import.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            List<RoomBooking> imported = manager.importRoomBookingsFromCsv(path);
            System.out.println("Успешно импортировано бронирований: " + imported.size());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}