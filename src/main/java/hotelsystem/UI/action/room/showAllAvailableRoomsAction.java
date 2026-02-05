package hotelsystem.UI.action.room;

import hotelsystem.Exception.ManagerHotelException;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class showAllAvailableRoomsAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(showAllAvailableRoomsAction.class);
    private final ManagerHotel managerHotel;

    public showAllAvailableRoomsAction(ManagerHotel managerHotel) {
        this.managerHotel = managerHotel;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения свободных номеров");
        try {
            System.out.println("\n=== СПИСОК СВОБОДНЫХ НОМЕРОВ ===");
            var rooms = managerHotel.getRooms(SortType.NONE, true);

            System.out.println("Доступно номеров: " + rooms.size());
            System.out.println("-".repeat(30));

            if (rooms.isEmpty()) {
                System.out.println("Свободных номеров нет");
                logger.info("Свободных номеров нет");
            } else {
                rooms.forEach(System.out::println);
                logger.info("Отображено {} свободных номеров", rooms.size());
            }

            System.out.println("-".repeat(30));

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении свободных номеров: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении свободных номеров: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}