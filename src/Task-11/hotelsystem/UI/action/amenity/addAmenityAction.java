package hotelsystem.UI.action.amenity;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Amenity;

import java.util.Scanner;

public class addAmenityAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addAmenityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\nДобавление услуги:");
            System.out.print("Название: ");
            String name = scanner.nextLine();
            System.out.print("Цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            manager.addAmenity(new Amenity(name, price));
            System.out.println("Услуга добавлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}