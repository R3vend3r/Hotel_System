package model;

import java.util.*;

public class RoomBooking extends Order {
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private double toralPrice;
    private List<AmenityOrder> services;

    public RoomBooking() {
    }

    public RoomBooking(String id, Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(id, client, totalPrice, checkInDate, checkOutDate);
        setRoom(room);
        setCheckOutDate(checkOutDate);
        setCheckInDate(checkInDate);
        setTotalPrice(totalPrice);
        this.services = new ArrayList<>();

    }

    public RoomBooking(Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(client, totalPrice,checkInDate, checkOutDate);
        setRoom(room);
        setCheckOutDate(checkOutDate);
        setCheckInDate(checkInDate);
        this.services = new ArrayList<>();
    }

    public Room getRoom() { return room; }

    public void setRoom(Room room) {
        this.room = Objects.requireNonNull(room, "Room cannot be null");
    }

    public Date getCheckOutDate() { return checkOutDate; }
    public List<AmenityOrder> getServices() { return Collections.unmodifiableList(services); }

    public void addService(AmenityOrder service) {
        services.add(service);
        setTotalPrice(getTotalPrice() + service.getAmenity().getPrice());
    }
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = Objects.requireNonNull(checkOutDate, "Check-out date cannot be null");
        setAvailableDate(checkOutDate);
    }

    public double getToralPrice() {
        return toralPrice;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setToralPrice(double toralPrice) {
        this.toralPrice = toralPrice;
    }
}