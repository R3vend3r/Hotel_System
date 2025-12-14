package hotelsystem.UI.action.import_export;

import hotelsystem.UI.action.Action;
import hotelsystem.controller.ManagerHotel;
import hotelsystem.Utils.HotelConfig;
import hotelsystem.dependencies.annotation.Inject;

import java.util.Scanner;

public class loadStateAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);
    @Inject
    private HotelConfig hotelConfig;
    public loadStateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nЗагрузка состояния\nВведите путь к файлу (или оставьте пустым для default): ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            filePath = hotelConfig.getDatabaseFilePath();
        }

        try {
            manager.loadStateFromJson(filePath);
            System.out.println("Состояние успешно загружено из файла: " + filePath);
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке состояния: " + e.getMessage());
            System.out.println("Использован путь: " + filePath);
            System.out.println("Проверьте конфигурацию в hotel.properties");
        }
    }
}