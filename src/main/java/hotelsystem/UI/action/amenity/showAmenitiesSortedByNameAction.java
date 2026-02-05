package hotelsystem.UI.action.amenity;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAmenitiesSortedByNameAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAmenitiesSortedByNameAction.class);
    private final ManagerHotel manager;

    public showAmenitiesSortedByNameAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг по названию");
        try {
            System.out.println("\nУслуги (по названию):");
            manager.getAmenities(SortType.ALPHABET)
                    .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                            a.getName(), a.getPrice()));

            logger.info("Услуги по названию успешно отображены");

        }  catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг по названию: {}", e.getMessage(), e);
            System.out.println("Ошибка при отображении услуг по названию: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг по названию: {}",
                    e.getMessage(), e);
            System.out.println("неожиданная ошибка: " + e.getMessage());
        }
    }
}