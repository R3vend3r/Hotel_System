package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;
import enums.SortType;

public class showAllAvailableRoomsAction implements Action {

    private final ManagerHotel managerHotel;

    public showAllAvailableRoomsAction(ManagerHotel managerHotel) {
        this.managerHotel = managerHotel;
    }

    @Override
    public void execute() {
        System.out.println("\n=== СПИСОК СВОБОДНЫХ НОМЕРОВ ===");
        var rooms = managerHotel.getRooms(SortType.NONE, true);

        System.out.println("Доступно номеров: " + rooms.size());
        System.out.println("----------------------------------");

        if (rooms.isEmpty()) {
            System.out.println("Свободных номеров нет");
        } else {
            rooms.values().forEach(System.out::println);
        }

        System.out.println("----------------------------------");
    }
}