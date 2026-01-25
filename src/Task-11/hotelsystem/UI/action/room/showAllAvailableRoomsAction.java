package hotelsystem.UI.action.room;

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
        logger.info("showAllAvailableRoomsAction: Начало отображения свободных номеров");
        try {
            System.out.println("\n=== СПИСОК СВОБОДНЫХ НОМЕРОВ ===");
            var rooms = managerHotel.getRooms(SortType.NONE, true);

            System.out.println("Доступно номеров: " + rooms.size());
            System.out.println("----------------------------------");

            if (rooms.isEmpty()) {
                System.out.println("Свободных номеров нет");
                logger.info("showAllAvailableRoomsAction: Свободных номеров нет");
            } else {
                rooms.forEach(System.out::println);
                logger.info("showAllAvailableRoomsAction: Отображено {} свободных номеров", rooms.size());
            }

            System.out.println("----------------------------------");

        } catch (Exception e) {
            logger.error("showAllAvailableRoomsAction: Ошибка при отображении свободных номеров: {}",
                    e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}