package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showRoomsSortedByPriceAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по цене):");
        manager.getRooms(SortType.PRICE, false)
                .forEach(r -> System.out.printf("%d - %.2f руб.%n",
                        r.getNumberRoom(), r.getPriceForDay()));
    }
}