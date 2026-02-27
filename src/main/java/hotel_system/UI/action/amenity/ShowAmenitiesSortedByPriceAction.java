package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.AmenityController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAmenitiesSortedByPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAmenitiesSortedByPriceAction.class);
    private final AmenityController amenityController;

    public ShowAmenitiesSortedByPriceAction(AmenityController amenityController) {
        this.amenityController = amenityController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг по цене");
        try {
            System.out.println("\nУслуги (по цене):");
            amenityController.getAmenities(SortType.PRICE)
                    .forEach(a -> System.out.printf("%.2f руб. - %s%n",
                            a.getPrice(), a.getName()));

            logger.info("Услуги по цене успешно отображены");

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг по цене: {}", e.getMessage(), e);
            System.out.println("Ошибка при отображении услуг по цене: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг по цене: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}