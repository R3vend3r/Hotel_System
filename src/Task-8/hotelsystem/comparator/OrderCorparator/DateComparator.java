package hotelsystem.comparator.OrderCorparator;

import hotelsystem.model.Order;

import java.util.Comparator;

public class DateComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getAvailableDate().compareTo(o2.getAvailableDate());
    }
}
