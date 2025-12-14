package hotelsystem.comparator.Room;

import hotelsystem.model.Room;

import java.util.Comparator;

public class PriceComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
        return Double.compare(o1.getPriceForDay(), o2.getPriceForDay());
    }
}
