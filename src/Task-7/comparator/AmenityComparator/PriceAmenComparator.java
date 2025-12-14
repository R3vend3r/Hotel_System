package comparator.AmenityComparator;

import model.AmenityOrder;

import java.util.Comparator;

public class PriceAmenComparator implements Comparator<AmenityOrder> {
    @Override
    public int compare(AmenityOrder o1, AmenityOrder o2) {
        return Double.compare(o1.getAmenity().getPrice(), o2.getAmenity().getPrice());
    }
}
