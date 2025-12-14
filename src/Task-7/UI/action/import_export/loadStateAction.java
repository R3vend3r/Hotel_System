package UI.action.import_export;

import UI.action.Action;
import Controller.ManagerHotel;
import Utils.HotelConfig;

import java.util.Scanner;

public class loadStateAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public loadStateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nЗагрузка состояния\nВведите путь к файлу (или оставьте пустым для default): ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            filePath = HotelConfig.getDatabaseFilePath();
        }

        try {
            manager.loadStateFromJson(filePath);
            System.out.println("Состояние успешно загружено из файла: " + filePath);
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке состояния: " + e.getMessage());
        }
    }
}