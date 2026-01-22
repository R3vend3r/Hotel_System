package hotelsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Setter
@Getter
public class HotelState {
    private Map<Integer, Room> rooms;
    private List<Client> clients;
    private List<Amenity> amenities;
    private List<RoomBooking> activeBookings;
    private List<RoomBooking> completedBookings;
    private List<AmenityOrder> amenityOrders;
    private Map<Integer, Queue<Client>> roomHistory;

    public HotelState() {
    }

    public HotelState(Map<Integer, Room> rooms, List<Client> clients, List<Amenity> amenities, List<RoomBooking> activeBookings, List<RoomBooking> completedBookings, List<AmenityOrder> amenityOrders, Map<Integer, Queue<Client>> roomHistory) {
        this.rooms = rooms;
        this.clients = clients;
        this.amenities = amenities;
        this.activeBookings = activeBookings;
        this.completedBookings = completedBookings;
        this.amenityOrders = amenityOrders;
        this.roomHistory = roomHistory;
    }

}