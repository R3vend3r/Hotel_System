package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showRoomsSortedByStarsAction implements Action {
    private final ManagerHotel manager;

    public showRoomsSortedByStarsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nНомера (сортировка по звездам):");
        manager.getRooms(SortType.STARS, false).values()
                .forEach(r -> System.out.printf("%d - %d★%n",
                        r.getNumberRoom(), r.getStars()));
    }
}