package hotelsystem.interfaceClass;

import hotelsystem.enums.SortType;
import hotelsystem.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    void addRoomBooking(RoomBooking booking);
    void addAmenityOrder(AmenityOrder order);
    Optional<RoomBooking> findActiveBookingByRoom(int roomNumber);
    List<AmenityOrder> getAmenityOrders();
    List<RoomBooking> getCompletedBookings();
    double calculatingTotalIncome();
    double calculateAmenityCost(int roomNumber);
    List<RoomBooking> getSortedBookings(SortType sortType);
    List<AmenityOrder> getSortedAmenityOrders(SortType sortType);
    void clearAll();
    List<AmenityOrder> getAmenityOrdersForBooking(String bookingId);
}