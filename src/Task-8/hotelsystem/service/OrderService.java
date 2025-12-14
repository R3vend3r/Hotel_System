package hotelsystem.service;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.enums.SortType;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.interfaceClass.IOrderRepository;
import hotelsystem.model.*;

import java.util.Date;
import java.util.List;

@Component
public class OrderService implements IClearable {
    @Inject
    private  IOrderRepository orderRepository;

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

    @Override
    public void clear() {
        orderRepository.clearAll();
    }
}