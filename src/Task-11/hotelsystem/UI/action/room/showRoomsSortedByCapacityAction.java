package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showRoomsSortedByCapacityAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по вместимости):");
        manager.getRooms(SortType.CAPACITY, false)
                .forEach(r -> System.out.printf("%d - %d чел.%n",
                        r.getNumberRoom(), r.getCapacity()));
    }
}