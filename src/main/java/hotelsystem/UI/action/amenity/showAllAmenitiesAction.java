package hotelsystem.UI.action.amenity;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAllAmenitiesAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAllAmenitiesAction.class);
    private final ManagerHotel manager;

    public showAllAmenitiesAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения всех услуг");
        try {
            System.out.println("\nСписок услуг:");
            manager.getAmenities(SortType.NONE)
                    .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                            a.getName(), a.getPrice()));

            logger.info("Все услуги успешно отображены");

        }  catch (ManagerHotelException e) {
            logger.error("Ошибка при отображение услуг: {}", e.getMessage(), e);
            System.out.println("Ошибка при отображение услуг: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении услуг: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}