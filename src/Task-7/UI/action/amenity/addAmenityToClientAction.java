package UI.action.amenity;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.Date;
import java.util.Scanner;

public class addAmenityToClientAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addAmenityToClientAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\nДобавление услуги клиенту:");
            System.out.print("Номер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Название услуги: ");
            String amenityName = scanner.nextLine();

            manager.addAmenityToClient(roomNumber,
                    manager.findAmenityByName(amenityName).orElseThrow(),
                    new Date());

            System.out.println("Услуга добавлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}