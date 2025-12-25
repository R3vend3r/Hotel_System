package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

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