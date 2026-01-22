package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showAvailableRoomsSortedByCapacityAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСвободные номера (по вместимости):");
        manager.getRooms(SortType.CAPACITY, true)
                .forEach(r -> System.out.printf("%d - %d чел. (%s)%n",
                        r.getNumberRoom(), r.getCapacity(), r.getType()));
    }
}