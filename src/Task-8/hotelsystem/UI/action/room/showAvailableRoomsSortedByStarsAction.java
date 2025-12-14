package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showAvailableRoomsSortedByStarsAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsSortedByStarsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСвободные номера (по звездам):");
        manager.getRooms(SortType.STARS, true).values()
                .forEach(r -> System.out.printf("%d - %d★ (%s)%n",
                        r.getNumberRoom(), r.getStars(), r.getType()));
    }
}