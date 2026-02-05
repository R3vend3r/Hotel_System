package hotelsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class RoomBooking extends Order {
    @Getter
    private Room room;
    @Setter
    @Getter
    private Date checkInDate;
    @Getter
    private Date checkOutDate;
    @Setter
    @Getter
    private double totalPrice;
    @Setter
    private int roomNumber;
    @Getter
    @Setter
    private Client client;
    @Setter
    private String clientInfo;
    private List<AmenityOrder> services;

    public RoomBooking() {
    }

    public RoomBooking(String id, Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(id, client.getId(), totalPrice, checkInDate, checkOutDate);
        setRoom(room);
        setCheckOutDate(checkOutDate);
        setClient(client);
        setCheckInDate(checkInDate);
        setTotalPrice(totalPrice);
        this.roomNumber = room != null ? room.getNumberRoom() : 0;
        this.services = new ArrayList<>();
        this.clientInfo = client.getName() + " " + client.getSurname();
    }

    public RoomBooking(Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(client.getId(), totalPrice,checkInDate, checkOutDate);
        setRoom(room);
        setClient(client);
        setRoomNumber(room.getNumberRoom());
        setCheckOutDate(checkOutDate);
        setCheckInDate(checkInDate);
        this.services = new ArrayList<>();
        this.clientInfo = client.getName() + " " + client.getSurname();
    }

    public void setRoom(Room room) {
        this.room = Objects.requireNonNull(room, "Room cannot be null");
    }

    public List<AmenityOrder> getServices() { return Collections.unmodifiableList(services); }

    public void addService(AmenityOrder service) {
        services.add(service);
        this.totalPrice += service.getAmenity().getPrice();
    }
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = Objects.requireNonNull(checkOutDate, "Check-out date cannot be null");
        setAvailableDate(checkOutDate);
    }

    public int getRoomNumber() {
        if (room != null) {
            return room.getNumberRoom();
        }
        return roomNumber;
    }

    public String getClientInfo() {
        if (clientInfo != null && !clientInfo.isEmpty()) {
            return clientInfo;
        } else if (client != null) {
            return client.getName() + " " + client.getSurname();
        } else if (getClientId() != null) {
            return "ID: " + getClientId();
        } else {
            return "Клиент не найден";
        }
    }

}