package hotelsystem.comparator.Room;

import hotelsystem.model.Room;

import java.util.Comparator;

public class RoomTypeComparator implements Comparator<Room> {

    @Override
    public int compare(Room o1, Room o2) {
        return o1.getType().compareTo(o2.getType());
    }
}
