package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.Scanner;

public class updateRoomPriceAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public updateRoomPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\nИзменение цены:");
            System.out.print("Номер комнаты: ");
            int number = scanner.nextInt();
            System.out.print("Новая цена: ");
            double price = scanner.nextDouble();

            manager.updateRoomPrice(number, price);
            System.out.println("Цена обновлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            scanner.nextLine();
        }
    }
}