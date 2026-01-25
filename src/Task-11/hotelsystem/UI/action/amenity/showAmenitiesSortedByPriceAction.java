package hotelsystem.UI.action.amenity;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAmenitiesSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAmenitiesSortedByPriceAction.class);
    private final ManagerHotel manager;

    public showAmenitiesSortedByPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.info("showAmenitiesSortedByPriceAction: Начало отображения услуг по цене");
        try {
            System.out.println("\nУслуги (по цене):");
            manager.getAmenities(SortType.PRICE)
                    .forEach(a -> System.out.printf("%.2f руб. - %s%n",
                            a.getPrice(), a.getName()));

            logger.info("showAmenitiesSortedByPriceAction: Услуги по цене успешно отображены");

        } catch (Exception e) {
            logger.error("showAmenitiesSortedByPriceAction: Ошибка при отображении услуг по цене: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}