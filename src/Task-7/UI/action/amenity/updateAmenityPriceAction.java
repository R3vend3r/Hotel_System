package UI.action.amenity;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.Scanner;

public class updateAmenityPriceAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public updateAmenityPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\nИзменение цены услуги:");
            System.out.print("Название услуги: ");
            String name = scanner.nextLine();
            System.out.print("Новая цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            manager.updateAmenityPrice(name, price);
            System.out.println("Цена обновлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}