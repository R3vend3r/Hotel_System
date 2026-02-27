package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.AmenityController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAmenitiesSortedByNameAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAmenitiesSortedByNameAction.class);
    private final AmenityController amenityController;

    public ShowAmenitiesSortedByNameAction(AmenityController amenityController) {
        this.amenityController = amenityController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения услуг по названию");
        try {
            System.out.println("\nУслуги (по названию):");
            amenityController.getAmenities(SortType.ALPHABET)
                    .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                            a.getName(), a.getPrice()));

            logger.info("Услуги по названию успешно отображены");

        }  catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении услуг по названию: {}", e.getMessage(), e);
            System.out.println("Ошибка при отображении услуг по названию: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг по названию: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}