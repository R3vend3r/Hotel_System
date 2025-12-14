package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.RoomCondition;

import java.util.Scanner;

public class changeRoomStatusAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public changeRoomStatusAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\nСмена статуса:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новый статус (1-READY, 2-ON_REPAIR, 3-CLEANING_REQUIRED): ");
            int status = scanner.nextInt();

            manager.updateRoomStatus(number, RoomCondition.values()[status-1]);
            System.out.println("Статус обновлен");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            scanner.nextLine();
        }
    }
}