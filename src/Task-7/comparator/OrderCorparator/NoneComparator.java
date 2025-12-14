package comparator.OrderCorparator;

import model.Order;

import java.util.Comparator;

public class NoneComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return 0;
    }
}