package hotel_system.comparator.Room;
import hotel_system.model.entity.Room;
import java.util.Comparator;

public class CapacityComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
        return Integer.compare(o1.getCapacity(), o2.getCapacity());
    }
}
