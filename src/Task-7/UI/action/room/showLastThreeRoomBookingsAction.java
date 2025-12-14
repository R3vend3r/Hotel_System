package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import model.Client;

import java.util.List;
import java.util.Scanner;

public class showLastThreeRoomBookingsAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showLastThreeRoomBookingsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nПоследние постояльцы\nВведите номер: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        List<Client> history = manager.getRoomHistory(roomNumber);
        int limit = Math.min(history.size(), 3);

        if (limit == 0) {
            System.out.println("История для комнаты " + roomNumber + " пуста");
        } else {
            System.out.println("Последние " + limit + " постояльца комнаты " + roomNumber + ":");
            history.subList(0, limit).forEach(client ->
                    System.out.println("- " + client.getName() + " " + client.getSurname())
            );
        }
    }
}