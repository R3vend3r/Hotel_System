package UI.action.room;

import UI.action.Action;
import Controller.ManagerHotel;
import model.Client;

import java.util.List;
import java.util.Scanner;

public class getFullRoomHistoryAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public getFullRoomHistoryAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nПолная история номера\nВведите номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        List<Client> history = manager.getRoomHistory(roomNumber);
        if (history.isEmpty()) {
            System.out.println("История для комнаты " + roomNumber + " пуста");
        } else {
            System.out.println("Полная история комнаты " + roomNumber + ":");
            history.forEach(client ->
                    System.out.println("- " + client.getName() +" " + client.getSurname() + " (ID: " + client.getId() + ")")
            );
        }
    }
}