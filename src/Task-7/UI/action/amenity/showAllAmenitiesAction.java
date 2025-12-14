package UI.action.amenity;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAllAmenitiesAction implements Action {
    private final ManagerHotel manager;

    public showAllAmenitiesAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nСписок услуг:");
        manager.getAmenities(SortType.NONE)
                .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                        a.getName(), a.getPrice()));
    }
}