package hotel_system.model.entity;

import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;
import hotel_system.model.converters.RoomConditionConverter;
import hotel_system.model.converters.RoomTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room implements Serializable {
    @Id
    @Column(nullable = false)
    private int number;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Convert(converter = RoomConditionConverter.class)
    @Column(name = "condition", nullable = false)
    private RoomCondition roomCondition;

    @Convert(converter = RoomTypeConverter.class)
    @Column(name = "type", nullable = false)
    private RoomType type;

    @Column(name = "price", nullable = false)
    private double priceForDay;

    @Column(name = "available_date")
    private Date availableDate;

    @Column(nullable = false)
    private int capacity;

    @Column
    private int stars;

    @Transient
    private String clientId;

    public Room() {
    }

    public Room(int number, RoomType type, double priceForDay, int capacity,
                RoomCondition condition, int stars) {
        setNumber(number);
        setType(type);
        setPriceForDay(priceForDay);
        setCapacity(capacity);
        setRoomCondition(condition);
        setStars(stars);
        this.isAvailable = true;
    }

    public Room(int number, RoomType type, double priceForDay, int capacity) {
        this(number, type, priceForDay, capacity,
                RoomCondition.READY, 0);
    }

    public void setPriceForDay(double priceForDay) {
        if (priceForDay <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.priceForDay = priceForDay;
    }

    public void setRoomCondition(RoomCondition status) {
        this.roomCondition = Objects.requireNonNull(status);
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public void occupy() {
        this.isAvailable = false;
    }

    public void vacate() {
        this.isAvailable = true;
    }
}