package hotel_system.comparator.OrderCorparator;

import hotel_system.model.entity.Order;

import java.util.Comparator;

public class AlphabetComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getClientId().compareTo(o2.getClientId());    }
}
