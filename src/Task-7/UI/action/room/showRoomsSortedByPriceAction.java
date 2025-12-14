package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showRoomsSortedByPriceAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по цене):");
        manager.getRooms(SortType.PRICE, false).values()
                .forEach(r -> System.out.printf("%d - %.2f руб.%n",
                        r.getNumberRoom(), r.getPriceForDay()));
    }
}