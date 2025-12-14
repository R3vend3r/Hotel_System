package model;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class HotelState {
    private Map<Integer, Room> rooms;
    private List<Client> clients;
    private List<Amenity> amenities;
    private List<RoomBooking> activeBookings;
    private List<RoomBooking> completedBookings;
    private List<AmenityOrder> amenityOrders;
    private Map<Integer, Queue<Client>> roomHistory;

    public HotelState() {}

    public HotelState(Map<Integer, Room> rooms,
                      List<Client> clients,
                      List<Amenity> amenities,
                      List<RoomBooking> activeBookings,
                      List<RoomBooking> completedBookings,
                      List<AmenityOrder> amenityOrders,
                      Map<Integer, Queue<Client>> roomHistory) {
        this.rooms = rooms;
        this.clients = clients;
        this.amenities = amenities;
        this.activeBookings = activeBookings;
        this.completedBookings = completedBookings;
        this.amenityOrders = amenityOrders;
        this.roomHistory = roomHistory;
    }

    public Map<Integer, Room> getRooms() { return rooms; }
    public void setRooms(Map<Integer, Room> rooms) { this.rooms = rooms; }

    public List<Client> getClients() { return clients; }
    public void setClients(List<Client> clients) { this.clients = clients; }

    public List<Amenity> getAmenities() { return amenities; }
    public void setAmenities(List<Amenity> amenities) { this.amenities = amenities; }

    public List<RoomBooking> getActiveBookings() { return activeBookings; }
    public void setActiveBookings(List<RoomBooking> activeBookings) { this.activeBookings = activeBookings; }

    public List<RoomBooking> getCompletedBookings() { return completedBookings; }
    public void setCompletedBookings(List<RoomBooking> completedBookings) { this.completedBookings = completedBookings; }

    public List<AmenityOrder> getAmenityOrders() { return amenityOrders; }
    public void setAmenityOrders(List<AmenityOrder> amenityOrders) { this.amenityOrders = amenityOrders; }

    public Map<Integer, Queue<Client>> getRoomHistory() {
        return roomHistory;
    }
    public void setRoomHistory(Map<Integer, Queue<Client>> roomHistory) {
        this.roomHistory = roomHistory;
    }}