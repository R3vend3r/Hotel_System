package UI.action.amenity;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAmenitiesSortedByNameAction implements Action {
    private final ManagerHotel manager;

    public showAmenitiesSortedByNameAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nУслуги (по названию):");
        manager.getAmenities(SortType.ALPHABET)
                .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                        a.getName(), a.getPrice()));
    }
}