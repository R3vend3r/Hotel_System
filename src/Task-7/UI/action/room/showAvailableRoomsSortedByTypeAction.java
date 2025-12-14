package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAvailableRoomsSortedByTypeAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByTypeAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСвободные номера (по типу):");
        manager.getRooms(SortType.TYPE, true).values()
                .forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                        r.getNumberRoom(), r.getType(), r.getPriceForDay()));
    }
}