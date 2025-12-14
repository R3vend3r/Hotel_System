package comparator.OrderCorparator;

import model.Order;

import java.util.Comparator;

public class AlphabetComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getClient().getName().compareTo(o2.getClient().getName());
    }
}
