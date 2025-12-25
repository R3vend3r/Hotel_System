package hotelsystem.UI.action.room;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;

public class showAvailableRoomsCountAction implements Action {
    private final ManagerHotel manager;

    public showAvailableRoomsCountAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.printf("\nСвободных номеров: %d%n",
                manager.getAvailableRoomsCount());
    }
}