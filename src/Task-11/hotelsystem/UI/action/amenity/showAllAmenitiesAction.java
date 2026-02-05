package hotelsystem.UI.action.amenity;

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
        logger.info("showAllAmenitiesAction: Начало отображения всех услуг");
        try {
            System.out.println("\nСписок услуг:");
            manager.getAmenities(SortType.NONE)
                    .forEach(a -> System.out.printf("%s - %.2f руб.%n",
                            a.getName(), a.getPrice()));

            logger.info("showAllAmenitiesAction: Все услуги успешно отображены");

        } catch (Exception e) {
            logger.error("showAllAmenitiesAction: Ошибка при отображении услуг: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}