package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showRoomsSortedByCapacityAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по вместимости):");
        manager.getRooms(SortType.CAPACITY, false).values()
                .forEach(r -> System.out.printf("%d - %d чел.%n",
                        r.getNumberRoom(), r.getCapacity()));
    }
}