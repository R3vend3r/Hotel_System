package hotelsystem.UI.action.client;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showClientsSortedByNameAction implements Action {
    private final ManagerHotel manager;

    public showClientsSortedByNameAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nКлиенты (по алфавиту):");
        manager.getAllActiveBookings(SortType.ALPHABET)
                .forEach(c -> System.out.println(c.getClient().getName() + " " + c.getClient().getSurname()));
    }
}