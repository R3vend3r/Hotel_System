package model;

import Utils.HotelConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import enums.RoomCondition;
import enums.RoomType;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Room implements Serializable {
    private String id;
    private int numberRoom;
    @JsonProperty("available")
    private boolean isAvailable;
    private RoomCondition roomCondition;
    private RoomType type;
    private double priceForDay;
    private Date availableDate;
    private int capacity;
    private int stars;
    private String clientId;

    private Queue<Client> clientHistory = new LinkedList<>();
    private static final HotelConfig config = new HotelConfig();

    public Room() {
    }

    public Room(String id, int number, RoomType type, double priceForDay, int capacity,
                RoomCondition condition, int stars) {
        setId(id);
        setNumberRoom(number);
        setType(type);
        setPriceForDay(priceForDay);
        setCapacity(capacity);
        setRoomCondition(condition);
        setStars(stars);

        this.isAvailable = true;
    }

    public Room(int number, RoomType type, double priceForDay, int capacity) {
        this(generateId(), number, type, priceForDay, capacity,
                RoomCondition.READY, 3);
    }

    private static String generateId() {
        return "RM-" + System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumberRoom(int numberRoom) {
        this.numberRoom = numberRoom;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public void clearRoom() {
        isAvailable = true;
        roomCondition = RoomCondition.CLEANING_REQUIRED;
        clientId = null;
        availableDate = null;
    }

    public void addClientToHistory(Client client) {
        Objects.requireNonNull(client, "Client cannot be null");
        clientHistory.add(client);
        // Ограничиваем размер истории согласно конфигурации
        while (clientHistory.size() > HotelConfig.getMaxHistoryEntries()) {
            clientHistory.poll();
        }
    }

    public int getNumberRoom() {
        return numberRoom;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void changeAvailability() {
        this.isAvailable = !isAvailable;
    }

    public RoomType getType() {
        return type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientIdAndDateAvailable(String clientId, Date availableDate) {
        this.clientId = Objects.requireNonNull(clientId);
        this.availableDate = Objects.requireNonNull(availableDate);
    }

    public double getPriceForDay() {
        return priceForDay;
    }

    public void setPriceForDay(double priceForDay) {
        if (priceForDay <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.priceForDay = priceForDay;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public RoomCondition getRoomCondition() {
        return roomCondition;
    }

    public void setRoomCondition(RoomCondition status) {
        if (!config.isRoomStatusChangeEnabled()) {
            throw new IllegalStateException("Изменение статуса комнаты отключено в настройках");
        }
        this.roomCondition = Objects.requireNonNull(status);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5");
        }
        this.stars = stars;
    }

    public Queue<Client> getClientHistory() {
        return new LinkedList<>(clientHistory);
    }
    @Override
    public String toString() {
        return "Room{" +
                "number=" + numberRoom +
                ", type=" + type +
                ", available=" + isAvailable +
                ", condition=" + roomCondition +
                ", price=" + priceForDay +
                ", capacity=" + capacity +
                ", stars=" + stars +
                '}';
    }
}