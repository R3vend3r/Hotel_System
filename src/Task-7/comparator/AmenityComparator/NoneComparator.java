package comparator.AmenityComparator;

import model.AmenityOrder;

import java.util.Comparator;

public class NoneComparator implements Comparator<AmenityOrder> {
    @Override
    public int compare(AmenityOrder o1, AmenityOrder o2) {
        return 0;
    }
}