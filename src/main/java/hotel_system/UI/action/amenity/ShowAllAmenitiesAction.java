package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowAllAmenitiesAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowAllAmenitiesAction.class);
    private final ManagerHotel manager;

    public ShowAllAmenitiesAction(ManagerHotel manager) {
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