package hotel_system.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client implements Serializable {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(name = "room_number")
    private Integer  roomNumber;

    public Client() {
    }

    public Client(String id, String name, String surname, Integer  roomNumber) {
        setId(id);
        setName(name);
        setSurname(surname);
        setRoomNumber(roomNumber);
    }

    public Client(String name, String surname) {
        this(generateId(), name, surname, null);
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    public void setSurname(String surname) {
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
        if (surname.isBlank()) {
            throw new IllegalArgumentException("Surname cannot be blank");
        }
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "Client ID cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Client ID cannot be blank");
        }
    }

    public void setRoomNumber(Integer roomNumber) {
        if (roomNumber != null && roomNumber < 0) {
            throw new IllegalArgumentException("Room number cannot be negative");
        }
        this.roomNumber = roomNumber;
    }

    public void assignToRoom(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void vacateRoom() {
        this.roomNumber = 0;
    }
    @Override
    public String toString() {
        return String.format("Client[id=%s, name=%s, surname=%s, room=%d]",
                id, name, surname, roomNumber);
    }
    private static String generateId() {
        return "CL-" + UUID.randomUUID().toString().substring(0, 8);
    }

}