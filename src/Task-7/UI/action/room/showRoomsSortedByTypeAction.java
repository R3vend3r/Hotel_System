package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showRoomsSortedByTypeAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByTypeAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по типу):");
        manager.getRooms(SortType.TYPE, false).values()
                .forEach(System.out::println);
    }
}