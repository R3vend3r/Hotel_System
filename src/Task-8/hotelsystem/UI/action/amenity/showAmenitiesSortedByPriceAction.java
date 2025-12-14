package hotelsystem.UI.action.amenity;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;

public class showAmenitiesSortedByPriceAction implements Action {
    private final ManagerHotel manager;

    public showAmenitiesSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("\nУслуги (по цене):");
        manager.getAmenities(SortType.PRICE)
                .forEach(a -> System.out.printf("%.2f руб. - %s%n",
                        a.getPrice(), a.getName()));
    }
}