package hotel_system.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "room_bookings")
public class RoomBooking extends Order {

    @ManyToOne
    @JoinColumn(name = "room_number")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client;

    @Transient
    private String clientInfo;

    public RoomBooking() {
    }

    public RoomBooking(String id, Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(id, client.getId(), totalPrice, checkInDate, checkOutDate);
        setRoom(room);
        setCheckOutDate(checkOutDate);
        setClient(client);
        setCheckInDate(checkInDate);
        this.clientInfo = client.getName() + " " + client.getSurname();
    }

    public RoomBooking(Client client, Room room, double totalPrice, Date checkInDate, Date checkOutDate) {
        super(client.getId(), totalPrice, checkInDate, checkOutDate);
        setRoom(room);
        setCheckOutDate(checkOutDate);
        setClient(client);
        setCheckInDate(checkInDate);
        this.clientInfo = client.getName() + " " + client.getSurname();
    }

    public void setRoom(Room room) {
        this.room = Objects.requireNonNull(room, "Room cannot be null");
    }

    public void setCheckOutDate(Date checkOutDate) {
        setAvailableDate(checkOutDate);
    }

    public int getRoomNumber() {
        return room != null ? room.getNumber() : 0;
    }
}