package hotelsystem.repository;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.interfaceClass.*;
import hotelsystem.enums.SortType;
import hotelsystem.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderRepository implements IOrderRepository {
    private final Map<String, RoomBooking> activeBookings = new HashMap<>();
    private final Map<String, AmenityOrder> amenityOrders = new HashMap<>();

    @Override
    public void addRoomBooking(RoomBooking booking) {
        activeBookings.put(booking.getId(), booking);
    }

    @Override
    public Optional<RoomBooking> findActiveBookingByRoom(int roomNumber) {
        return activeBookings.values().stream()
                .filter(b -> b.getRoom().getNumberRoom() == roomNumber)
                .findFirst();
    }

    @Override
    public void addAmenityOrder(AmenityOrder order) {
        amenityOrders.put(order.getId(), order);
    }

    @Override
    public List<AmenityOrder> getAmenityOrders() {
        return new ArrayList<>(amenityOrders.values());
    }

    @Override
    public List<RoomBooking> getCompletedBookings() {
        return new ArrayList<>(activeBookings.values());
    }

    @Override
    public double calculatingTotalIncome() {
        double bookingsIncome = activeBookings.values().stream()
                .mapToDouble(RoomBooking::getTotalPrice)
                .sum();

        double amenitiesIncome = amenityOrders.values().stream()
                .mapToDouble(AmenityOrder::getTotalPrice)
                .sum();

        return bookingsIncome + amenitiesIncome;
    }

    @Override
    public double calculateAmenityCost(int roomNumber) {
        return activeBookings.values().stream()
                .filter(b -> b.getRoom().getNumberRoom() == roomNumber)
                .flatMap(b -> b.getServices().stream())
                .mapToDouble(AmenityOrder::getTotalPrice)
                .sum();
    }

    @Override
    public List<RoomBooking> getSortedBookings(SortType sortType) {
        Comparator<RoomBooking> comparator = switch (sortType) {
            case ALPHABET -> Comparator.comparing(b -> b.getClientId());
            case DATE_END -> Comparator.comparing(RoomBooking::getCheckOutDate);
            case PRICE -> Comparator.comparing(RoomBooking::getTotalPrice);
            default -> (a, b) -> 0;
        };
        return activeBookings.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<AmenityOrder> getSortedAmenityOrders(SortType sortType) {
        Comparator<AmenityOrder> comparator = switch (sortType) {
            case DATE_END -> Comparator.comparing(AmenityOrder::getServiceDate);
            case PRICE -> Comparator.comparing(AmenityOrder::getTotalPrice);
            default -> (a, b) -> 0;
        };
        return amenityOrders.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<AmenityOrder> getAmenityOrdersForBooking(String bookingId) {
        return amenityOrders.values().stream()
                .filter(o -> o.getClientId().equals(bookingId))
                .collect(Collectors.toList());
    }

    @Override
    public void clearAll() {
        activeBookings.clear();
        amenityOrders.clear();
    }
}