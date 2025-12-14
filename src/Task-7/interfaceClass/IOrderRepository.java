package interfaceClass;

import enums.SortType;
import model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    void createRoomBooking(Client client, Room room,
                                  Date checkInDate, Date checkOutDate);
    AmenityOrder addAmenityOrder(Client client, Amenity amenity, Date serviceDate);
    void completeRoomBooking(int roomNumber, Date checkOutDate);
    List<RoomBooking> getActiveBookings();
    double calculateTotalIncome();
    double calculateAmenityCost(int roomNumber);
    double calculateStayCost(double priceForDay, Date checkInDate, Date endDate);
    List<AmenityOrder> getAmenityOrders();
    List<RoomBooking> getCompletedBookings();
    List<RoomBooking> getSortedBookings(SortType sortType);
    List<AmenityOrder> getSortedAmenityOrders(SortType sortType);
    List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber);
    Optional<RoomBooking> findActiveBookingByRoom(int roomNumber);
    void clearAll();
}