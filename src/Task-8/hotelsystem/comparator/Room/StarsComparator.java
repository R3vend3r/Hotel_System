package hotelsystem.comparator.Room;

import hotelsystem.model.Room;

import java.util.Comparator;

public class StarsComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
        return Integer.compare(o1.getStars(), o2.getStars());
    }
}
