package UI.action.amenity;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

import java.util.Scanner;

public class showClientAmenitiesSortedByDateAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showClientAmenitiesSortedByDateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nУслуги клиента (по дате)\nНомер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        manager.findClientByRoom(roomNumber).ifPresent(client ->
                manager.getClientAmenitiesSorted(client, SortType.DATE_END)
                        .forEach(a -> System.out.printf("%s - %s%n",
                                a.getServiceDate(), a.getAmenity().getName()))
        );
    }
}