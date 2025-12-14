package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAllRoomsAction implements Action {
    private final ManagerHotel manager;

    public showAllRoomsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nВсе номера:");
        manager.getRooms(SortType.NONE, false).values().forEach(System.out::println);
        System.out.println("Всего: " + manager.getRooms(SortType.NONE, false).size());
    }
}