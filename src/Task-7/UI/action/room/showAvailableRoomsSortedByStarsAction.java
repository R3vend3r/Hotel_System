package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

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