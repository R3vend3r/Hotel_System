package hotelsystem.UI.action.client;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;

public class showClientCountAction implements Action {
    private final ManagerHotel manager;

    public showClientCountAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.printf("\nОбслужено клиентов: %d%n",
                manager.getClientCount());
    }
}