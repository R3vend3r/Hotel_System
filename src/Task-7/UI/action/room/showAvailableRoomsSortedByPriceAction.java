package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAvailableRoomsSortedByPriceAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСвободные номера (по цене):");
        manager.getRooms(SortType.PRICE, true).values()
                .forEach(r -> System.out.printf("%d - %s (%.2f руб.)%n",
                        r.getNumberRoom(), r.getType(), r.getPriceForDay()));
    }
}