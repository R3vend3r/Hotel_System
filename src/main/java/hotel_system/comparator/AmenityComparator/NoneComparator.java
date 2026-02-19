package hotel_system.comparator.AmenityComparator;

import hotel_system.model.entity.AmenityOrder;

import java.util.Comparator;

public class NoneComparator implements Comparator<AmenityOrder> {
    @Override
    public int compare(AmenityOrder o1, AmenityOrder o2) {
        return 0;
    }
}