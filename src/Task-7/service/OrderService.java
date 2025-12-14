package service;

import enums.SortType;
import interfaceClass.IOrderRepository;
import model.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderService {
    private final IOrderRepository orderRepository;

    public OrderService(IOrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
    }

    public void createRoomBooking(Client client, Room room, Date checkInDate, Date checkOutDate) {
        orderRepository.createRoomBooking(client, room, checkInDate, checkOutDate);
    }

    public void addAmenityToBooking(int roomNumber, Amenity amenity, Date serviceDate) {
        RoomBooking booking = getActiveBookingForRoom(roomNumber);
        AmenityOrder order = createAmenityOrder(booking, amenity, serviceDate);
        addOrderToBooking(booking, order);
    }

    private RoomBooking getActiveBookingForRoom(int roomNumber) {
        return orderRepository.findActiveBookingByRoom(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("No active booking for room " + roomNumber));
    }

    private AmenityOrder createAmenityOrder(RoomBooking booking, Amenity amenity, Date serviceDate) {
        return orderRepository.addAmenityOrder(booking.getClient(), amenity, serviceDate);
    }

    private void addOrderToBooking(RoomBooking booking, AmenityOrder order) {
        booking.addService(order);
    }

    public void completeRoomBooking(int roomNumber, Date checkOutDate) {
        orderRepository.completeRoomBooking(roomNumber, checkOutDate);
    }

    public double calculateTotalIncome() {
        return orderRepository.calculateTotalIncome();
    }

    public List<RoomBooking> getActiveBookingsSorted(SortType sortType) {
        return orderRepository.getSortedBookings(sortType);
    }

    public double calculateAmenityCost(int roomNumber) {
        return orderRepository.calculateAmenityCost(roomNumber);
    }

    public List<RoomBooking> getCompletedBookings() {
        return orderRepository.getCompletedBookings();
    }

    public List<AmenityOrder> getAmenityOrdersSorted(SortType sortType) {
        return orderRepository.getSortedAmenityOrders(sortType);
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        return orderRepository.getLastThreeBookingsForRoom(roomNumber);
    }

    public void clearAll() {
        orderRepository.clearAll();
    }
}