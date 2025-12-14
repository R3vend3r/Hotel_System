package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

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