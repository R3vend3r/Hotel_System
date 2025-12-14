package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

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