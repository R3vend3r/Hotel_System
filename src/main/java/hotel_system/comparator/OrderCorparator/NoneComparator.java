package hotel_system.comparator.OrderCorparator;

import hotel_system.model.entity.Order;

import java.util.Comparator;

public class NoneComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return 0;
    }
}