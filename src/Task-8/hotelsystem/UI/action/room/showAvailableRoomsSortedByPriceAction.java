package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

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