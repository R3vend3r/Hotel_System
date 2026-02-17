package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAllRoomsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAllRoomsAction.class);
    private final ManagerHotel manager;

    public showAllRoomsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения всех номеров");
        try {
            System.out.println("\nВсе номера:");
            var rooms = manager.getRooms(SortType.NONE, false);
            rooms.forEach(System.out::println);

            System.out.println("Всего: " + rooms.size());
            logger.info("Отображено {} всех номеров", rooms.size());

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении всех номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении всех номеров: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}