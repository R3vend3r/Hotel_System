package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAvailableRoomsSortedByCapacityAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByCapacityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСвободные номера (по вместимости):");
        manager.getRooms(SortType.CAPACITY, true).values()
                .forEach(r -> System.out.printf("%d - %d чел. (%s)%n",
                        r.getNumberRoom(), r.getCapacity(), r.getType()));
    }
}