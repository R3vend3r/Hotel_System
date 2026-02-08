package hotel_system.comparator.Room;

import hotel_system.model.entity.Room;

import java.util.Comparator;

public class NoneComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
        return 0;
    }
}