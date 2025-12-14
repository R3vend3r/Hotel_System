package UI.action.client;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showClientsSortedByCheckoutDateAction implements Action {
    private final ManagerHotel manager;

    public showClientsSortedByCheckoutDateAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nКлиенты (по дате выезда):");
        manager.getAllActiveBookings(SortType.DATE_END)
                .forEach(c -> System.out.printf("%s %s (выезд: %s)%n",
                        c.getClient().getName(), c.getClient().getSurname(), c.getCheckOutDate()));
    }
}