package UI.action.order;

import Controller.ManagerHotel;
import UI.action.Action;

public class showTotalRevenueAction implements Action {
    private final ManagerHotel manager;

    public showTotalRevenueAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.printf("\nОбщий доход: %.2f руб.%n",
                manager.calculateTotalIncome());
    }
}