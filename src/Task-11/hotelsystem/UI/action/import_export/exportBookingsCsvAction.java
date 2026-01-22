package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.controller.ManagerHotel;
import hotelsystem.Exception.DataExportException;
import hotelsystem.enums.SortType;

import java.util.Scanner;

public class exportBookingsCsvAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public exportBookingsCsvAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Экспорт бронирований в CSV ===");
            System.out.print("Введите путь для сохранения (пример: data/bookings_export.csv): ");
            String path = scanner.nextLine().trim();

            if (path.isEmpty()) throw new IllegalArgumentException("Путь не может быть пустым");

            manager.exportRoomBookingsToCsv(path);
            System.out.println("Успешно экспортировано бронирований: " +
                    (manager.getAllActiveBookings(SortType.NONE).size() +
                            manager.getAllCompletedBookings().size()));
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } catch (DataExportException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Системная ошибка: " + e.getMessage());
        }
    }
}