package comparator.AmenityComparator;

import model.AmenityOrder;

import java.util.Comparator;
import java.util.Date;

public class DateAmenComparator implements Comparator<AmenityOrder> {
    @Override
    public int compare(AmenityOrder o1, AmenityOrder o2) {
        Date date1 = o1.getServiceDate();
        Date date2 = o2.getServiceDate();
        return date1.compareTo(date2);
    }
}
