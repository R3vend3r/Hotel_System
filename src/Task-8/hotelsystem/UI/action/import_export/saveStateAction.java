package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.controller.ManagerHotel;
import hotelsystem.Utils.HotelConfig;
import hotelsystem.dependencies.annotation.Inject;

import java.util.Scanner;

public class saveStateAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    @Inject
    private HotelConfig hotelConfig;

    public saveStateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nСохранение состояния\nВведите путь к файлу (или оставьте пустым для default): ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            filePath = hotelConfig.getDatabaseFilePath();
        }

        try {
            manager.saveStateToJson(filePath);
            System.out.println("Состояние успешно сохранено в файл: " + filePath);
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении состояния: " + e.getMessage());
        }
    }
}